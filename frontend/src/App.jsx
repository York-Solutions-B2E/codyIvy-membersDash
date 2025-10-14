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
  const navigate = useNavigate();

  const handleLogin = async (tokenResponse) => {
    try {
      const res = await fetch("https://www.googleapis.com/oauth2/v3/userinfo", {
        headers: {
          Authorization: `Bearer ${tokenResponse.access_token}`,
        },
      });

      const profile = await res.json();
      setUser(profile);
      console.log("User info:", profile);
      navigate("/dashboard", { replace: true });
    } catch (error) {
      console.error("Failed to fetch user info:", error);
    }}

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
                <Dashboard user={user} />
              </RequireAuth>
            }
          />
        </Routes>
      </>
    );
  };

export default App;
