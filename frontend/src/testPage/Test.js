import React, {useEffect, useState} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUserCircle } from '@fortawesome/free-regular-svg-icons';
import './Test.css';
import {useLocation} from "react-router-dom";



const Test = () => {
    const location = useLocation();
    const questions = location.state?.questions || [];

    return(
      <div className="Test">Тест</div>
    );
}
export default Test;