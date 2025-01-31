import React, { useState } from 'react';
import './MainPage.css';

const handleLogout= () => {
    window.location = '/registration-form'
}

const MainPage = () => {
    return (
        <div className="MainPage">
            <button className="logout" onClick={handleLogout}>Выход</button>
        </div>
    );
}
export default MainPage;