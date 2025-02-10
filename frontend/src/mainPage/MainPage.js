import React, {useEffect, useState} from 'react';
import './MainPage.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome"
import {faFolderClosed} from "@fortawesome/free-solid-svg-icons";
import KeyboardBackspaceIcon from '@mui/icons-material/KeyboardBackspace';

const MainPage = () => {
    const [folders, setFolders] = useState([]);
    const [tests, setTests] = useState([]);
    const [currentPath, setCurrentPath] = useState([]);
    const [selectedFolder, setSelectedFolder] = useState(null);
    const [subFolders, setSubFolders] = useState([]);
    const [folderTests, setFolderTests] = useState([]);
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
                 //   await fetchTestsFromFolder(parentFolderId);
                }
            } catch (error) {
                console.error('Ошибка при загрузке данных:', error.message);
            }
        };

        loadFolderData();
    }, [currentPath]);

    return (
        <div className="MainPage">
            <button className="logout" onClick={handleLogout}>Выход</button>

            <ul className="folders">

                {currentPath.length > 0 && (
                    <KeyboardBackspaceIcon className="back" onClick={() => handleBack(currentPath[currentPath.length - 1])} style={{ fontSize: '32px' }}>
                    </KeyboardBackspaceIcon>
                )}

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
                                <button className="start">Пройти</button>
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
                                    <button className="start">Пройти</button>
                                </div>
                            ))
                        ) : (
                            <p></p>
                        )}
                    </div>
                )}
        </div>
    )};
    export default MainPage;