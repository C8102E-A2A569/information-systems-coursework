import React, { useState } from 'react';
import './RegistrationForm.css';
import {useNavigate} from "react-router-dom";

const RegistrationForm = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');
    const [formData, setFormData] = useState({
        name: '',
        login: '',
        password: ''
    });

    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
        setErrorMessage('');
    };
    const handleValidation = () => {
        const {name, login, password} = formData;
        if (login.length < 4) {
            return 'Длина логина меньше 4';
        }
        if (password.length < 4) {
            return 'Длина пароля меньше 4';
        }
        if (name.length < 4) {
            return 'Длина имени меньше 4';
        }
        if (name.length > 30) {
            return 'Длина имени больше 30';
        }
        if (login.length > 25) {
            return 'Длина логина больше 25';
        }
        if (password.length > 20) {
            return 'Длина пароля больше 20';
        }
        return null;
    };
    const handleSubmit = async (e) => {
        e.preventDefault();
        const url = isLogin
            ? 'http://localhost:8080/auth/sign-in'
            : 'http://localhost:8080/auth/sign-up';

        if (!isLogin) {
            const validationError = handleValidation();
            if (validationError) {
                setErrorMessage(validationError);
                return;
            }
        }
        try {
            const dataToSend = {
                login: formData.login,
                password: formData.password,
                name: formData.name
            };
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(dataToSend),
            });
            console.log('Form submitted:', dataToSend);

            if (!response.ok) {
                const errorData = await response.json();
                const errorMessage = errorData.message;
                setErrorMessage(errorMessage);
                console.log(errorMessage);
            } else {
                setErrorMessage('')
                const data = await response.json();
                localStorage.setItem('token', data.token);
                localStorage.setItem('name', data.name);
                localStorage.setItem('login', formData.login)
                console.log(data.token)
                console.log(formData.name)
                navigate('/main-page');
            }

        } catch (error) {
            console.error('Ошибка:', error);
        }
    };


    const toggleForm = (isLogin) => {
        setIsLogin(isLogin);
    };
    const errorMessageStyle = {
        top: isLogin ? '195px' : '155px'
    };

    return (
        <div className="RegistrationComponent">
            <div className="FormContainer">
                <div className="button-container">
                    <button
                        className={`ToggleButton ${isLogin ? 'active' : ''}`}
                        onClick={() => toggleForm(true)}
                        disabled={isLogin}
                    >
                        Вход
                    </button>
                    <button
                        className={`ToggleButton ${!isLogin ? 'active' : ''}`}
                        onClick={() => toggleForm(false)}
                        disabled={!isLogin}
                    >
                        Регистрация
                    </button>
                </div>
                {errorMessage && <div className="error-message" style={errorMessageStyle}>{errorMessage}</div>}
                {isLogin ? (
                    <form onSubmit={handleSubmit} id='form1'>
                        <div class="input-container">
                            <label>Логин</label>
                            <input
                                className='login'
                                type="text"
                                name="login"
                                value={formData.login}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div class="input-container">
                            <label>Пароль</label>
                            <input
                                className='password'
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <button type="submit">Отправить</button>
                    </form>
                ) : (
                    <form onSubmit={handleSubmit}>
                        <div class="input-container">
                            <label>Логин</label>
                            <input
                                className='login'
                                type="text"
                                name="login"
                                value={formData.login}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div class="input-container">
                            <label>Имя</label>
                            <input
                                className='name'
                                type="text"
                                name="name"
                                value={formData.name}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <div class="input-container">
                            <label>Пароль</label>
                            <input
                                className='password'
                                type="password"
                                name="password"
                                value={formData.password}
                                onChange={handleChange}
                                required
                            />
                        </div>
                        <button type="submit">Отправить</button>
                    </form>
                )}
            </div>
        </div>
    );
};

export default RegistrationForm;
