import {
  BrowserRouter as Router,
  Routes,
  Route,
  useNavigate,
} from "react-router-dom";
import "./App.css";
import NavBar from "./components/navBar/NavBar.jsx";
import Dashboard from "./pages/dashboard/Dashboard.jsx";
import Login from "./pages/login/Login";
import React from "react";
import RequireAuth from "./components/RequireAuth/RequireAuth.jsx";

function App() {
  const [user, setUser] = React.useState(null);
  const [idToken, setIdToken] = React.useState(null);

  const navigate = useNavigate();

  React.useEffect(() => {
    if (user) {
      navigate("/dashboard", { replace: true });
    }
  }, [user]);

  const handleLogin = async (tokenResponse) => {
    setIdToken(tokenResponse.id_token);
    try {
      const response = await fetch("http://localhost:8080/api/auth/me", {
        method: "GET",
        headers: {
          Authorization: `Bearer ${tokenResponse.id_token}`,
        },
      });
      if (response.ok) {
        const userInfo = await response.json();
        setUser(userInfo);
        
      } else {
        console.error("Failed to fetch user info");
      }
    } catch (error) {
      console.error("Error fetching user info:", error);
    }
  };

  return (
    <>
      <NavBar
        user={user}
        onSignOut={() => {
          setUser(null);
          navigate("/", { replace: true });
        }}
      />
      <Routes>
        <Route path="/" element={<Login onLogin={handleLogin} />} />
        <Route
          path="/dashboard"
          element={
            <RequireAuth user={user}>
              <Dashboard idToken={idToken} user={user} />
            </RequireAuth>
          }
        />
      </Routes>
    </>
  );
}

export default App;
