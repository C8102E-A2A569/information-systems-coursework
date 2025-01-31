import React, { useState } from 'react';
import './RegistrationForm.css';

const RegistrationForm = () => {
    const [isLogin, setIsLogin] = useState(true);
    const [errorMessage, setErrorMessage] = useState('');
    const [formData, setFormData] = useState({
        username: '',
        login: '',
        password: ''
    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // Здесь вы можете добавить логику для отправки данных на сервер
        console.log('Form submitted:', formData);
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
                                name="username"
                                value={formData.username}
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
