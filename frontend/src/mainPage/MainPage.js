import React, {useEffect, useState} from 'react';
import './MainPage.css';
import {faFolderClosed} from "@fortawesome/free-solid-svg-icons";
import {faPlus} from "@fortawesome/free-solid-svg-icons"
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSearch } from '@fortawesome/free-solid-svg-icons';
import KeyboardBackspaceIcon from '@mui/icons-material/KeyboardBackspace';
import UserProfile from "../mainPageComponents/UserProfile";
import Groups from "../mainPageComponents/Groups";
import {useNavigate} from "react-router-dom";
import groups from "../mainPageComponents/Groups";

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
    const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
    const [testTitle, setTestTitle] = useState('');
    const [withPoints, setWithPoints] = useState(false);
    const [foundTest, setFoundTest] = useState(null);
    const [groups, setGroups] = useState([]);

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
            console.log(data);
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
    const createTestClick = () => {
        setIsCreateModalOpen(true);
    };

    const closeCreateTestModal = () => {
        setIsCreateModalOpen(false);
    };

    const handleCreateClick = () =>{
        const currentFolderId = currentPath.length > 0
            ? currentPath[currentPath.length - 1]
            : null;

        navigate('/create-test', {
            state: {
                title: testTitle,
                points: withPoints,
                folderId: currentFolderId
            }
        });
    }

    const handleSearch = async () => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                console.error('Токен не найден');
                return;
            }

            const response = await fetch(`http://localhost:8080/tests/${searchId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error('Тест не найден');
            }

            const testData = await response.json();
            console.log('Найденный тест:', testData);
            setFoundTest(testData);
            setErrorMessage('');

        } catch (error) {
            console.error('Ошибка при поиске теста:', error.message);
            setFoundTest(null);
            setErrorMessage('Тест не найден');
        }
        // Здесь можно добавить логику для отображения найденного теста
        // navigate('/test-page', {
        //     state: {
        //         testInfo: {
        //             id: testData.id,
        //             name: testData.name,
        //             points: testData.points
        //         },
        //         questions: testData.questions
        //     }
        // });
        //
        // closeSearchModal();

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
        if (e.target.className !== 'modal' && e.target.className !== 'create-modal') return;
        closeModal();
        closeSearchModal();
        closeCreateTestModal();
    };
    const handleStartTest = async (testId) => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                console.error('Токен не найден');
                return;
            }
            const response = await fetch(`http://localhost:8080/tests/training/${testId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });
            if (!response.ok) {
                console.error('Ошибка при получении вопросов');
            }
            const testData = await response.json();
            console.log(testData);
            navigate('/test-page', {
                state: {
                    testInfo: {
                        id: testData.id,
                        name: testData.name,
                        points: testData.points
                    },
                    questions: testData.questions
                }
            });
        } catch (error) {
            console.error('Ошибка при загрузке данных теста:', error);
        }
    };
    const fetchUserGroups = async () => {
        try {
            const token = localStorage.getItem('token');
            if (!token) {
                console.error('Токен не найден');
                return;
            }

            const response = await fetch('http://localhost:8080/groups/my', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
            });

            if (!response.ok) {
                throw new Error('Ошибка при получении групп');
            }

            const data = await response.json();
            setGroups(data);
        } catch (error) {
            console.error('Ошибка при загрузке групп:', error.message);
        }
    };


    useEffect(() => {
        const loadFolderData = async () => {
            try {
                //  await fetchFolders();
                if (currentPath.length === 0) {
                    await fetchFolders();
                    await fetchTests();
                    setFolderTests([]);
                } else {
                    const parentFolderId = currentPath[currentPath.length - 1];
                    await fetchSubfolders(parentFolderId);
                    await fetchTestsFromFolder(parentFolderId);
                }
                await fetchUserGroups();
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
                    <div className="addFolder" onClick={openModal}><FontAwesomeIcon icon={faPlus}/></div>
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
                    <div className="createTestButton" onClick={createTestClick}>
                        <FontAwesomeIcon icon={faPlus}/>
                        <div className="createTest">Создать тест</div>
                    </div>
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
                    <div className="createTestButton" onClick={createTestClick}>
                        <FontAwesomeIcon icon={faPlus}/>
                        <div className="createTest">Создать тест</div>
                    </div>
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

            <Groups groups={groups}></Groups>

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
                        {/*<input*/}
                        {/*    type="text"*/}
                        {/*    value={searchId}*/}
                        {/*    onChange={(e) => setSearchId(e.target.value)}*/}
                        {/*    placeholder="Введите id"*/}
                        {/*/>*/}
                        {/*<button className="search-button" onClick={handleSearch}>Найти</button>*/}
                        {!foundTest && !errorMessage && (
                            <>
                                <input
                                    type="text"
                                    value={searchId}
                                    onChange={(e) => setSearchId(e.target.value)}
                                    placeholder="Введите id теста"
                                />
                                <button className="search-button" onClick={handleSearch}>
                                    Найти
                                </button>
                            </>
                        )}

                        {errorMessage && (
                            <div className="search-result">
                                <div className="error-message">{errorMessage}</div>
                                <button
                                    className="retry-button"
                                    onClick={() => setErrorMessage('')}
                                >
                                    Попробовать снова
                                </button>
                            </div>
                        )}
                        {foundTest && (
                            <div className="search-result">
                                <div className="found-test">
                                    <h3>{foundTest.name}</h3>
                                    <div className="test-points">
                                        Баллов: {foundTest.points}
                                    </div>
                                    <button
                                        className="start-test-button"
                                        onClick={() => {
                                            handleStartTest(foundTest.id);
                                            closeSearchModal();
                                        }}
                                    >
                                        Пройти тест
                                    </button>
                                    <button
                                        className="close-result-button"
                                        onClick={() => {
                                            setFoundTest(null);
                                            setErrorMessage('');
                                        }}
                                    >
                                        Закрыть
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            )}


            {isCreateModalOpen && (
                <div className="create-modal" >
                    <div className="create-modal-content" >
                        <h2>Создать тест</h2>
                        <button onClick={closeCreateTestModal} className="close-create-modal">✖</button>
                        <label>
                            Название теста:
                            <input
                                type="text"
                                value={testTitle}
                                onChange={(e) => setTestTitle(e.target.value)}
                            />
                        </label>
                        <label className="points">
                            <input
                                type="checkbox"
                                checked={withPoints}
                                onChange={() => setWithPoints(!withPoints)}
                            />
                            <div className="withPoints">С баллами</div>
                        </label>
                        <button className="ok" onClick={handleCreateClick}>Oк</button>
                    </div>
                </div>
            )}
        </div>
    )};
export default MainPage;