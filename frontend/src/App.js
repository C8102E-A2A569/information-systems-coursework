import './App.css';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import RegistrationForm from './authentication/RegistrationForm';
import MainPage from './mainPage/MainPage';
import Test from './testPage/Test';
function App() {
  return (
      <Router>
          <Routes>
              <Route path="/registration-form" element={<RegistrationForm />} />
              <Route path="/main-page" element={<MainPage />} />
              <Route path="/test-page" element={<Test />} />
          </Routes>
      </Router>
  );
}

export default App;
