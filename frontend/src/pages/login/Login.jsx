import React from "react";
import LoginContainer from "../../components/LoginContainer/LoginContainer.jsx";
import { useLocation, useNavigate } from "react-router-dom";

export default function Login({ onLogin }) {
  const navigate = useNavigate();
  const location = useLocation();
    const from = location.state?.from?.pathname || "/dashboard";

  const handleLogin = (tokenResponse) => {
    if (onLogin) onLogin(tokenResponse);
    navigate(from, { replace: true }); // Navigate to the original location after login
  };

  return (
    <div>
      <LoginContainer onLogin={handleLogin} />
    </div>
  );
}
