import React, {useEffect, useState} from 'react';
import './MainPage.css';
import {faFolderClosed} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import KeyboardBackspaceIcon from '@mui/icons-material/KeyboardBackspace';
import UserProfile from "../mainPageComponents/UserProfile";
import Groups from "../mainPageComponents/Groups";
import {useNavigate} from "react-router-dom";

const MainPage = () => {
    const [folders, setFolders] = useState([]);
    const [tests, setTests] = useState([]);
    const [currentPath, setCurrentPath] = useState([]);
    const [selectedFolder, setSelectedFolder] = useState(null);
    const [subFolders, setSubFolders] = useState([]);
    const [folderTests, setFolderTests] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newFolder, setNewFolder] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [searchId, setSearchId] = useState('');
    const [isSearchModalOpen, setIsSearchModalOpen] = useState(false);

    const navigate = useNavigate();
    const handleLogout = () => {
        window.localStorage.removeItem('token');
        window.location = '/registration-form'
    }
    const fetchFolders = async () => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                console.error('Токен не найден');
                return;
            }
            const response = await fetch('http://localhost:8080/folders/root', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) {
                throw new Error('Ошибка при получении папок');
            }

            const data = await response.json();
            console.log('Полученные данные:', data)
            setFolders(data);
        } catch (error) {
            console.log('Ошибка при загрузке папок');
        }
    };

    const fetchTests = async () => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                console.error('Токен не найден');
                return;
            }
            const response = await fetch('http://localhost:8080/tests/user', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) {
                throw new Error('Ошибка при получении тестов');
            }

            const data = await response.json();
            console.log('Полученные данные:', data)
            setTests(data);
        } catch (error) {
            console.log('Ошибка при загрузке тестов');
        }
    };


    const fetchSubfolders = async (folderId) => {
        const subFoldersResponse = await fetch('http://localhost:8080/folders/subfolders', {
        method: 'POST',
            headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json',
        },
            body: JSON.stringify({ id: folderId }),
    });
    if (!subFoldersResponse.ok) {
        throw new Error('Ошибка при получении дочерних папок');
    }
        // setSubFolders(subFoldersData);
        return await subFoldersResponse.json();
}
const fetchTestsFromFolder = async (folderId) => {
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            console.error('Токен не найден');
            return [];
        }
        const folderTestsResponse = await fetch('http://localhost:8080/tests/folder', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({folderId: folderId}),
        });
        if (!folderTestsResponse.ok) {
            throw new Error('Ошибка при получении тестов папки');
        }
        return await folderTestsResponse.json();
        //setFolderTests(folderTestsData);
    } catch (error){
        return [];
    }
}


    const handleFolderClick = async (folderId) => {
        console.log(folderId);
        try {
            setCurrentPath(prevPath => [...prevPath, folderId]);

            const subFoldersData = await fetchSubfolders(folderId);
            console.log(subFoldersData)
            setFolders(subFoldersData);

            const folderTestsData = await fetchTestsFromFolder(folderId);
            console.log(folderTestsData);
            setFolderTests(folderTestsData);
        } catch (error) {
            console.error('Ошибка при загрузке данных для папки:', error.message);
        }
    };

    const handleBack = async (folderId) => {
        if (currentPath.length === 0) return;
        const newPath = [...currentPath];
        newPath.pop();
        setCurrentPath(newPath);
        if (newPath.length === 0) {
            await fetchFolders();
            await fetchTests();
            setFolderTests([]);
        } else {
            const parentFolderId = newPath[newPath.length - 1];

            const subFoldersData = await fetchSubfolders(parentFolderId);
            setFolders(subFoldersData);

            const folderTestsData = await fetchTestsFromFolder(parentFolderId);
            setFolderTests(folderTestsData);
        }
    };

    const openModal = () => {
        setIsModalOpen(true);
        setErrorMessage('');
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setNewFolder('');
    };

    const openSearchModal = () => {
        setIsSearchModalOpen(true);
        setSearchId('');
    };

    const closeSearchModal = () => {
        setIsSearchModalOpen(false);
        setSearchId('');
    };

    const handleSearch = () => {
        // Здесь будет логика для поиска по ID
        console.log('Идет поиск теста с ID:', searchId);
        // Реализация поиска...
        closeSearchModal(); // Закрываем модальное окно после поиска
    };

    const handleCreateFolder = async () => {
        const token = localStorage.getItem('token');
        if (!token) {
            console.error('Токен не найден');
            return;
        }
        const currentFolderId = currentPath[currentPath.length - 1];
        const response = await fetch('http://localhost:8080/folders/create', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: newFolder,
                parentFolderId: currentFolderId || null,
            }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            setErrorMessage( 'Произошла ошибка при создании папки');
        } else {
            const newFolder = await response.json();
            setFolders(prevFolders => [newFolder, ...prevFolders]);
            closeModal();
        }
    };

    const handleOutsideClick = (e) => {
        if (e.target.className !== 'modal') return;
        closeModal();
        closeSearchModal();
    };
    const handleStartTest = async (testId) => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                console.error('Токен не найден');
                return;
            }
            const response = await fetch(`http://localhost:8080/tests/start`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) {
                throw new Error('Ошибка при получении вопросов');
            }
            const questionsData = await response.json();
            navigate('/test-page', { state: { questions: questionsData } });
        } catch (error) {
            console.error('Ошибка при загрузке данных теста:', error);
        }
    };

    useEffect(() => {
        const loadFolderData = async () => {
            try {
                if (currentPath.length === 0) {
                    await fetchFolders();
                    await fetchTests();
                    setFolderTests([]);
                } else {
                    const parentFolderId = currentPath[currentPath.length - 1];
                    await fetchSubfolders(parentFolderId);
                    await fetchTestsFromFolder(parentFolderId);
                }
            } catch (error) {
                console.error('Ошибка при загрузке данных:', error.message);
            }
        };

        loadFolderData();
    }, [currentPath]);

    return (
        <div className="MainPage" onClick={handleOutsideClick}>
            <button className="logout" onClick={handleLogout}>Выход</button>
            <button className="search" onClick={openSearchModal}>
                <FontAwesomeIcon icon={faSearch} className="icon"/> Найти тест
            </button>
            <UserProfile></UserProfile>
            <ul className="folders">
                <div className="header">
                {currentPath.length > 0 && (
                    <KeyboardBackspaceIcon className="back"
                                           onClick={() => handleBack(currentPath[currentPath.length - 1])}
                                           style={{ fontSize: '32px' }}>
                    </KeyboardBackspaceIcon>
                )}
                    <div className="placeholder"></div>
                    <div className="addFolder" onClick={openModal}>+</div>
                </div>
                {folders && folders.length > 0 ? (
                    folders.map((folder, index) => (
                        <div
                            className="folder"
                            key={index}
                            onClick={() => handleFolderClick(folder.id)}
                        >
                            {folder.name}
                            <FontAwesomeIcon icon={faFolderClosed} className="folderIcon" />
                        </div>
                    ))
                ) : (
                    <p></p>
                )}
            </ul>

            {currentPath.length > 0 && (
                <div className="tests">
                    {folderTests && folderTests.length > 0 ? (
                        folderTests.map((test, index) => (
                            <div className="test" key={index}>
                                {test.name} ({test.points} баллов)
                                <br/>
                                <button className="start"  onClick={() => handleStartTest(test.id)}>Пройти</button>
                            </div>
                        ))
                    ) : (
                        <p></p>
                    )}
                </div>
            )}

            {currentPath.length === 0 && tests.length >= 0 && (
                    <div className="tests">
                        {tests && tests.length > 0 ? (
                            tests.map((test, index) => (
                                <div className="test" key={index}>
                                    {test.name} ({test.points} баллов)
                                    <br/>
                                    <button className="start" onClick={() => handleStartTest(test.id)}>Пройти</button>
                                </div>
                            ))
                        ) : (
                            <p></p>
                        )}
                    </div>
                )}

            <Groups></Groups>

            {isModalOpen && (
                <div className="modal">
                    <div className="modal-content">
                        <h3>Новая папка</h3>
                        {errorMessage && <p style={{ color: '#7f0000' }}>{errorMessage}</p>}
                        <input
                            type="text"
                            value={newFolder}
                            onChange={(e) => {
                                setNewFolder(e.target.value);
                                setErrorMessage('');
                            }}
                            placeholder="Введите название папки"
                        />
                        <div className="modal-actions">
                            <button onClick={handleCreateFolder}>Создать</button>
                            <button onClick={closeModal}>Отмена</button>
                        </div>
                    </div>
                </div>
            )}
            {isSearchModalOpen && (
                <div className="modal">
                    <div className="search-modal">
                        <input
                            type="text"
                            value={searchId}
                            onChange={(e) => setSearchId(e.target.value)}
                            placeholder="Введите id"
                        />
                        <button className="search-button" onClick={handleSearch}>Найти</button>
                    </div>
                </div>
            )}
        </div>
    )};
    export default MainPage;