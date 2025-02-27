import React, { useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faClipboardCheck, faTimes } from '@fortawesome/free-solid-svg-icons';
import './ResultsButton.css';

const ResultsButton = () => {
    const [results, setResults] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [isResultsVisible, setIsResultsVisible] = useState(false);

    const statusTranslations = {
        AWAITING_APPROVAL: 'Ожидает проверки',
        APPROVED: 'Проверен',
        REJECTED: 'Отклонен'
    };

    const fetchResults = async () => {
        try {
            setLoading(true);
            setError('');
            const token = localStorage.getItem('token');
            const response = await fetch('http://localhost:8080/results/my', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            if (!response.ok) throw new Error('Ошибка загрузки результатов');
            const data = await response.json();
            setResults(data);
            setIsResultsVisible(true);
        } catch (err) {
            setError('Не удалось загрузить результаты');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleShowDetails = (testId, repetitionNumber) => {
        console.log('Запрос деталей для:', testId, repetitionNumber);
    };

    const handleCloseResults = () => {
        setIsResultsVisible(false); // Скрыть окно
    };

    return (
        <div className="back">
            <button
                className="my-results"
                onClick={fetchResults}
                disabled={loading}
            >
                <FontAwesomeIcon icon={faClipboardCheck} className="icon" />
                {loading ? 'Загрузка...' : 'Результаты'}
            </button>

            {isResultsVisible && (
                <div className="results-section">
                    <button className="close-button" onClick={handleCloseResults}>
                        <FontAwesomeIcon icon={faTimes} />
                    </button>
                    {error && <div className="error-message">{error}</div>}
                    {results.length > 0 ? (
                        <div className="results-list">
                            {results.map((result) => (
                                <div
                                    key={`${result.testId}-${result.repetitionNumber}`}
                                    className="result-item"
                                    onClick={() =>
                                        handleShowDetails(result.testId, result.repetitionNumber)
                                    }
                                >
                                    <div className="result-header">
                                        <h4>{result.testName}</h4>
                                        <span
                                            className={`status ${result.status.toLowerCase()}`}
                                        >
                                            {statusTranslations[result.status] || result.status}
                                        </span>
                                    </div>
                                    <div className="result-details">
                                        <p>Попытка: {result.repetitionNumber}</p>
                                        <p>Набрано баллов: {result.totalPoints}</p>
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="error-message">Нет доступных результатов</div>
                    )}
                </div>
            )}
        </div>
    );
};

export default ResultsButton;