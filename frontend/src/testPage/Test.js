import React, {useEffect, useState} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCircle } from '@fortawesome/free-regular-svg-icons';
import './Test.css';
import {useLocation} from "react-router-dom";



const Test = () => {
    const location = useLocation();
    const [testInfo, setTestInfo] = useState(location.state?.testInfo || {});
    const questions = location.state?.questions || [];

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
                              <div>  <input type="text" placeholder="Введите ответ" /></div>
                          )}
                          {question.type === 'RADIOBUTTON' && (
                              question.answerOptions.map(option => (
                                  <div key={option.id} className="radio-option">
                                      <input type="radio" name={`question-${question.id}`} value={option.option} />
                                      <label>{option.option}</label>
                                  </div>
                              ))
                          )}
                          {question.type === 'CHECKBOX' && (
                              question.answerOptions.map(option => (
                                  <div key={option.id} className="checkbox-option">
                                      <input type="checkbox" value={option.option} />
                                      <label>{option.option}</label>
                                  </div>
                              ))
                          )}
                      </div>
                  ))
              ) : (
                  <p>Вопросы не найдены.</p>
              )}
              <button className="start" >Завершить</button>
          </div>
      </div>
        </div>
    );
}
export default Test;