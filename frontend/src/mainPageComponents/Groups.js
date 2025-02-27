import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './Groups.css';

const Groups = ({ groups }) => {
    return (
        <div className="groups">
            <h2>Мои группы</h2>
            {groups.length > 0 ? (
                <ul>
                    {groups.map((group, index) => (
                        <li key={index}>{group.name}</li>
                    ))}
                </ul>
            ) : (
                <p>У вас пока нет групп</p>
            )}
        </div>
    );
};

export default Groups;