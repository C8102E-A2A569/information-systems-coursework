import React, {useEffect, useState} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import './Groups.css';
import {faEdit, faPlus, faTasks, faTrash} from "@fortawesome/free-solid-svg-icons";

const Groups = ({ groups }) => {
    const [selectedGroup, setSelectedGroup] = useState(null);
    const [members, setMembers] = useState([]);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const fetchMembers = async (groupId) => {
        try {
            const response = await fetch(`http://localhost:8080/groups/${groupId}/users`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });

            if (!response.ok) throw new Error('Ошибка загрузки участников');

            const data = await response.json();
            setMembers(data);
        } catch (error) {
            console.error(error);
        }
    };
    const handleGroupClick = async (group) => {
        if (group.role === 'ADMIN') {
            await fetchMembers(group.id);
            setSelectedGroup(group);
            setIsModalOpen(true);
        }
    };
    const handleModalClose = () => {
        setIsModalOpen(false);
        setSelectedGroup(null);
    };

    const handleModalBackgroundClick = (e) => {
        if (e.target.classList.contains('modal-background')) {
            handleModalClose();
        }
    };
    const handleDeleteGroup = async () => {

    };

    return (
        <div className="groups">
            <h2>Мои группы</h2>
            {groups.length > 0 ? (
                    <ul className="group-list">
                        {groups.map((group) => (
                            <li
                                key={group.id}
                                className={`group-item ${group.role === 'ADMIN' ? 'admin' : ''}`}
                                onClick={() => handleGroupClick(group)}
                            >
                                {group.name}
                                {group.role === 'ADMIN' && (
                                    <FontAwesomeIcon
                                        icon={faTrash}
                                        className="delete-icon"
                                        onClick={(e) => {
                                            e.stopPropagation();
                                          //  handleDeleteGroup(group.id);
                                        }}
                                    />
                                )}
                            </li>
                        ))}
                    </ul>
            ) : (
                <p>У вас пока нет групп</p>
            )}
            {isModalOpen && selectedGroup && (
                <div className="modal-background" onClick={handleModalBackgroundClick}>
                <div className="group-modal">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h3>{selectedGroup.name}</h3>
                            <FontAwesomeIcon
                                icon={faEdit}
                                className="edit-icon"
                               // onClick={handleEdit}
                            />
                        </div>

                        <div className="members-list">
                            <h4>Участники:</h4>
                            {members.map(member => (
                                <div key={member.login} className="member-item">
                                    <span>{member.name} ({member.login})</span>
                                </div>
                            ))}
                        </div>

                        <div className="modal-actions">
                            <button>
                                <FontAwesomeIcon icon={faPlus} />
                                Добавить участников
                            </button>
                            <button >
                                <FontAwesomeIcon icon={faTasks} />
                                Дать тест
                            </button>
                        </div>
                    </div>
                </div>
                </div>
            )}
        </div>
    );
};

export default Groups;