import React, {useEffect, useState} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCircle } from '@fortawesome/free-regular-svg-icons';
import MainPage from "../mainPage/MainPage";
import './UserProfile.css';



const UserProfile = () => {
    const [isModalOpen, setModalOpen] = useState(false);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [name, setName] = useState('');
    const [errorMessage, setErrorMessage] = useState('aaaaaaaaaaa');

    useEffect(() => {
        const storedUsername = localStorage.getItem('login');
        const storedPassword = localStorage.getItem('password');
        const storedName = localStorage.getItem('name');

        if (storedUsername) setUsername(storedUsername);
        if (storedPassword) setPassword(storedPassword);
        if (storedName) setName(storedName);
    }, []);


    const openModal = () => {
        setModalOpen(true);
    };

    const closeModal = () => {
        setModalOpen(false);
    };

    // Функция для обработки отправки формы
    const handleSubmit = async (e) => {
        e.preventDefault();
        const token = localStorage.getItem('token');
        if (!token) {
            console.error('Token not found');
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/user/profile`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    login: username,
                    name: name,
                    password: password,
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Невозможно обновить данные');
            }

            const updatedUser = await response.json();
            localStorage.setItem('login', updatedUser.login);
            localStorage.setItem('name', updatedUser.name);
            localStorage.setItem('password', password);
            closeModal();
        } catch (error) {
            setErrorMessage(error.message);
        }
    };

    const handleOutsideClick = (e) => {
        if (e.target.classList.contains('modal2')) {
            closeModal();
        }
    };

    return (
        <div>
            <FontAwesomeIcon icon={faUserCircle} className="profile" onClick={openModal} />

            {isModalOpen && (
                <div className="modal2" onClick={handleOutsideClick}>
                    <div className="modal-content2">
                        <span className="close" onClick={closeModal}>&times;</span>
                        <h2>Редактировать профиль</h2>
                        {errorMessage && <div className="error">{errorMessage}</div>}
                        <form onSubmit={handleSubmit}>
                            <div>
                                <label htmlFor="username">Логин:</label>
                                <input
                                    id="username"
                                    type="text"
                                    value={username}
                                    onChange={(e) => setUsername(e.target.value)}
                                    required
                                />
                            </div>
                            <div>
                                <label htmlFor="name">Имя пользователя:</label>
                                <input
                                    id="name"
                                    type="text"
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                    required
                                />
                            </div>
                            <div>
                                <label htmlFor="password">Пароль:</label>
                                <input
                                    id="password"
                                    type="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    required
                                />
                            </div>
                            <button type="submit" className='sendButton'>Отправить</button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
};

export default UserProfile;