import React, {useEffect, useState} from 'react';
import './CreateTestPage.css';
import {useLocation} from "react-router-dom";
import {useNavigate} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus} from "@fortawesome/free-solid-svg-icons";

const CreateTestPage = () => {
    const location = useLocation();
    const { title: initialTitle, points } = location.state || {};
    const [title, setTitle] = useState(initialTitle || '');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [questionType, setQuestionType] = useState('TEXT');
    const [questionText, setQuestionText] = useState('');
    const [answerOptions, setAnswerOptions] = useState(['']);

    const handleAddQuestionClick = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        resetModalFields();
    };
    const resetModalFields = () => {
        setQuestionType('TEXT');
        setQuestionText('');
        setAnswerOptions(['']);
    };
    const handleQuestionTypeChange = (e) => {
        setQuestionType(e.target.value);
        if (e.target.value === 'TEXT') {
            setAnswerOptions(['']);
        }
    };

    const handleAddOption = () => {
        setAnswerOptions(prev => [...prev, '']);
    };

    const handleOptionChange = (index, value) => {
        const newOptions = [...answerOptions];
        newOptions[index] = value;
        setAnswerOptions(newOptions);
    };

    const handleSaveQuestion = () => {
        closeModal();
    };
    return(
        <div className="testPage">
            <div className="Test">
                <div className="test-header">
                    <input
                        type="text"
                        className="test-name"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}></input>
                </div>
            <div className="createQuestionButton" onClick={handleAddQuestionClick}>
                <FontAwesomeIcon icon={faPlus} />
                <div className="createQuestion">Добавить вопрос</div>
            </div>

    {isModalOpen && (
        <div className="modal-question" onClick={closeModal}>
            <div className="modal-question-content" onClick={(e) => e.stopPropagation()}>
                <h2>Добавить вопрос</h2>
                <label>
                    Название вопроса:
                    <input
                        type="text"
                        value={questionText}
                        onChange={(e) => setQuestionText(e.target.value)}
                    />
                </label>
                <label>
                    Тип вопроса:
                    <select value={questionType} onChange={handleQuestionTypeChange}>
                        <option value="TEXT">Текстовый ответ</option>
                        <option value="CHECKBOX">Чекбокс</option>
                        <option value="RADIOBUTTON">Радиокнопка</option>
                    </select>
                </label>
                {questionType !== 'TEXT' && (
                    <div>
                        <h3>Варианты ответа:</h3>
                        {answerOptions.map((option, index) => (
                            <div key={index}>
                                <input
                                    type="text"
                                    value={option}
                                    onChange={(e) => handleOptionChange(index, e.target.value)}
                                    placeholder={`Вариант ${index + 1}`}
                                />
                            </div>
                        ))}
                        <button onClick={handleAddOption}>Добавить вариант</button>
                    </div>
                )}
                <button onClick={handleSaveQuestion}>Сохранить вопрос</button>
                <button onClick={closeModal}>✖</button>
            </div>
        </div>
    )}
            </div>
</div>
);
};

export default CreateTestPage;