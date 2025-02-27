import React, {useState} from 'react';
import axios from 'axios';
import './CreateTestPage.css';
import {useLocation, useNavigate} from "react-router-dom";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus} from "@fortawesome/free-solid-svg-icons";

const CreateTestPage = () => {
    const location = useLocation();
    const {title: initialTitle, points} = location.state || {};
    const [title, setTitle] = useState(initialTitle || '');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [questionType, setQuestionType] = useState('TEXT');
    const [questionText, setQuestionText] = useState('');
    const [answerOptions, setAnswerOptions] = useState([{text: '', isCorrect: false}]);
    const [questionPoints, setQuestionPoints] = useState(1);
    const [questions, setQuestions] = useState([]);

    const navigate = useNavigate();
    const handleAddQuestionClick = () => {
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setIsModalOpen(false);
    };

    const resetModalFields = () => {
        setQuestionType('TEXT');
        setQuestionText('');
        setAnswerOptions([{text: '', isCorrect: false}]);
        setQuestionPoints(1);
    };

    const handleQuestionTypeChange = (e) => {
        setQuestionType(e.target.value);
        if (e.target.value === 'TEXT') {
            setAnswerOptions([{text: '', isCorrect: false}]);
        }
    };

    const handleAddOption = () => {
        setAnswerOptions(prev => [...prev, {text: '', isCorrect: false}]);
    };

    const handleOptionChange = (index, value) => {
        const newOptions = [...answerOptions];
        newOptions[index].text = value;
        setAnswerOptions(newOptions);
    };

    const handleCorrectOptionChange = (index) => {
        const newOptions = [...answerOptions];
        if (questionType === 'RADIOBUTTON') {
            newOptions.forEach((option, i) => {
                option.isCorrect = i === index;
            });
        } else {
            newOptions[index].isCorrect = !newOptions[index].isCorrect;
        }
        setAnswerOptions(newOptions);
    };
    const handleSaveQuestion = () => {
        if (!questionText.trim()) {
            alert('Введите текст вопроса');
            return;
        }
        if (points && questionPoints < 1) {
            alert('Укажите количество баллов больше 0');
            return;
        }
        const newQuestion = {
            question: questionText,
            points: points ? questionPoints : 0,
            type: questionType,
            answerOptions: answerOptions.map(option => ({option: option.text, isCorrect: option.isCorrect,})),
        };
        setQuestions(prev => [...prev, newQuestion]);
        closeModal();
        resetModalFields();
    };

    const totalPoints = questions.reduce((total, question) => total + question.points, 0);

    const handleSubmitTest = async () => {
        if (!title.trim()) {
            console.error('Название теста не может быть пустым.');
            return;
        }

        const token = localStorage.getItem('token');
        if (!token) {
            console.error('Токен не найден');
            return;
        }

        const dataToSend = {
            name: title,
            points: totalPoints,
            questions: questions.map(q => ({
                question: q.question,
                points: q.points,
                type: q.type,
                answerOptions: q.answerOptions.map(option => ({
                    option: option.option,
                    isCorrect: option.isCorrect,
                })),
            })),
        };

        try {
            const response = await fetch('http://localhost:8080/tests/create/new', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(dataToSend),
            });
            console.log(JSON.stringify(dataToSend));
            if (!response.ok) {
                throw new Error('Ошибка при создании теста');
            }

            const responseData = await response.json();
            navigate('/main-page');
        } catch (error) {
            console.error('Ошибка при создании теста:', error);
        }
    };

    return (
        <div className="testPage">
            <div className="Test">
                <div className="test-header">
                    <input
                        type="text"
                        className="test-name"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)} />
                    {points && (
                        <div className="total-points">Общее количество баллов: {totalPoints}</div>
                    )}
                </div>

    <div className="questions-list">
        {questions.map((question, index) => (
            <div key={index} className="question-item">
                <h4>{question.question}</h4> ({question.points} баллов)
                {question.answerOptions.map((option, i) => (
                    <div key={i} className="option-item">
                        <span>- {option.option} {option.isCorrect && '(Правильный)'}</span>
                    </div>
                ))}
            </div>
        ))}
    </div>

    <div className="createQuestionButton" onClick={handleAddQuestionClick}>
        <FontAwesomeIcon icon={faPlus}/>
        <div className="createQuestion">Добавить вопрос</div>
    </div>

    {
        isModalOpen && (
            <div className="modal-question" onClick={closeModal}>
                <div className="modal-question-content" onClick={(e) => e.stopPropagation()}>
                    <h2>Добавить вопрос</h2>
                    <button onClick={closeModal} className="close-button">✖</button>
                    <label>
                        Вопрос:
                        <input
                            type="text"
                            value={questionText}
                            onChange={(e) => setQuestionText(e.target.value)}/>
                    </label>
                    <label>
                        <div className="question-type">Тип вопроса:</div>
                        <select value={questionType} onChange={handleQuestionTypeChange}>
                            <option value="TEXT">Текстовый ответ</option>
                            <option value="RADIOBUTTON">Один вариант ответа</option>
                            <option value="CHECKBOX">Несколько вариантов ответа</option>
                        </select>
                    </label>
                    {questionType !== 'TEXT' && (
                        <div>
                            <h3>Варианты ответов:</h3>
                            {answerOptions.map((option, index) => (
                                <div key={index} className="answer-item">
                                    <input
                                        type="text"
                                        className="answer-options"
                                        value={option.text}
                                        onChange={(e) => handleOptionChange(index, e.target.value)}
                                        placeholder={`Вариант ${index + 1}`}/>
                                    <label className="is-correct">
                                        <input
                                            type={questionType === 'RADIOBUTTON' ? 'radio' : 'checkbox'}
                                            checked={option.isCorrect}
                                            onChange={() => handleCorrectOptionChange(index)}/>
                                        <div className="isCorrect">Правильный ответ</div>
                                    </label>
                                </div>
                            ))}
                            <button onClick={handleAddOption} className="add-option">Добавить вариант</button>
                        </div>
                    )}
                    {points && (
                        <label>
                            Количество баллов:
                            <input
                                type="number"
                                min="1"
                                max="100"
                                value={questionPoints}
                                onChange={(e) => setQuestionPoints(Number(e.target.value))}/>
                        </label>
                    )}
                    <button onClick={handleSaveQuestion} className="add-question">Сохранить вопрос</button>
                </div>
            </div>
        )
    }
    <button onClick={handleSubmitTest} className="submit-test">Завершить</button>
</div>
</div>
)
    ;
};

export default CreateTestPage;