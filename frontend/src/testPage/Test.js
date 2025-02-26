import React, {useEffect, useState} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCircle } from '@fortawesome/free-regular-svg-icons';
import './Test.css';
import {useLocation} from "react-router-dom";
import {useNavigate} from "react-router-dom";



const Test = () => {
    const location = useLocation();
    const [testInfo, setTestInfo] = useState(location.state?.testInfo || {});
    const questions = location.state?.questions || [];
    const [answers, setAnswers] = useState({});

    const navigate = useNavigate();
    const handleTextChange = (questionId, answer) => {
        setAnswers(prev => ({
            ...prev,
            [questionId]: { textAnswer: answer }
        }));
    };

    const handleRadioChange = (questionId, optionId) => {
        setAnswers(prev => ({
            ...prev,
            [questionId]: { radiobuttonAnswerId: optionId }
        }));
    };

    const handleCheckboxChange = (questionId, optionId, isChecked) => {
        setAnswers(prev => {
            const currentAnswers = prev[questionId]?.checkboxAnswersId || [];
            if (isChecked) {
                return {
                    ...prev,
                    [questionId]: { checkboxAnswersId: [...currentAnswers, optionId] }
                };
            } else {
                return {
                    ...prev,
                    [questionId]: { checkboxAnswersId: currentAnswers.filter(id => id !== optionId) }
                };
            }
        });
    };
    const handleSubmit = async () => {
        const results = {
            id: testInfo.id,
            questionsForCheck: Object.entries(answers).map(([questionId, answer]) => ({
                id: Number(questionId),
                ...answer
            }))
        };
        console.log(JSON.stringify(results));
        try {
            const response = await fetch('http://localhost:8080/tests/training/send', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${localStorage.getItem("token")}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(results),
            });

            if (!response.ok) {
                throw new Error('Ошибка при отправке результатов');
            } else {
                navigate("/main-page");
            }
        } catch (error) {
            console.error("Ошибка:", error);
        }
    };

    return(
        <div className="testPage">
      <div className="Test">
          <div className="test-header">
              <div className="test-name">{testInfo.name}</div>
              {testInfo?.points != null ? (
          <div className="points">Всего баллов: {testInfo.points}</div>
                  ) : null}
              <div className="questionsCount">Количество вопросов: {questions.length}</div>
          </div>
          <div className="questions">
              {questions.length ? (
                  questions.map((question, index) => (
                      <div key={question.id} className="question">
                        <p>{index + 1}. {question.question}</p>
                  {question.points != null && (
                              <div className="questionPoints">({question.points} баллов)</div>
                          )}
                          {question.type === 'TEXT' && (
                              <div>  <input type="text"
                                            placeholder="Введите ответ"
                                            onChange={(e) => handleTextChange(question.id, e.target.value)}/></div>
                          )}
                          {question.type === 'RADIOBUTTON' && (
                              question.answerOptions.map(option => (
                                  <div key={option.id} className="radio-option">
                                      <input type="radio" name={
                                          `question-${question.id}`}
                                             value={option.option}
                                             onChange={() => handleRadioChange(question.id, option.id)}
                                      />
                                      <label>{option.option}</label>
                                  </div>
                              ))
                          )}
                          {question.type === 'CHECKBOX' && (
                              question.answerOptions.map(option => (
                                  <div key={option.id} className="checkbox-option">
                                      <input
                                          type="checkbox"
                                          value={option.option}
                                          onChange={(e) => handleCheckboxChange(question.id, option.id, e.target.checked)}
                                      />
                                      <label>{option.option}</label>
                                  </div>
                              ))
                          )}
                      </div>
                  ))
              ) : (
                  <p>Вопросы не найдены.</p>
              )}
              <button className="start" onClick={handleSubmit} >Завершить</button>
          </div>
      </div>
        </div>
    );
}
export default Test;