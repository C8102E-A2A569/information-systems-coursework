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
    const handleSubmit = (e) => {
        e.preventDefault(); // Предотвращаем перезагрузку страницы
        console.log({ username, password, name });
        //отправка данных



        // if (response.ok) {
        //     localStorage.setItem('login', username);
        //     localStorage.setItem('password', password);
        //     localStorage.setItem('name', name);
        //     closeModal(); // Закрываем модальное окно после отправки
        // }
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