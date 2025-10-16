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

  React.useEffect(() => {
    if (user) {
      console.log("User is logged in:", user);
    }
  }, [user]);

  const handleLogin = (tokenResponse) => {
    setUser(tokenResponse);
    navigate("/dashboard", { replace: true });
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
                <Dashboard user={user} />
              </RequireAuth>
            }
          />
        </Routes>
      </>
    );
  };

export default App;
