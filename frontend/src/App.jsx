import {
  Routes,
  Route,
  useNavigate,  
  useLocation 
} from "react-router-dom";
import "./App.css";
import NavBar from "./components/navBar/NavBar.jsx";
import Dashboard from "./pages/dashboard/Dashboard.jsx";
import Login from "./pages/login/Login";
import React from "react";
import RequireAuth from "./components/RequireAuth/RequireAuth.jsx";
import ClaimsList from "./pages/claimsList/ClaimsList.jsx";
import ClaimDetails from "./pages/claimDetails/ClaimDetails.jsx";

function App() {
  const [user, setUser] = React.useState(null);
  const [idToken, setIdToken] = React.useState(() => localStorage.getItem("idToken"));
  const location = useLocation();

  const navigate = useNavigate();




  React.useEffect(() => {
    const token = localStorage.getItem("idToken");

    if (token && !user) {
      setIdToken(token);
      handleLogin({ id_token: token });
    }
  }, [user]);

  const handleLogin = async (tokenResponse) => {
    localStorage.setItem("idToken", tokenResponse.id_token);
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
          localStorage.removeItem("idToken");
          navigate("/", { replace: true });
        }}
      />
      <Routes>
        <Route path="/" element={<Login onLogin={handleLogin} />} />
        <Route
          path="/dashboard"
          element={
            <RequireAuth user={user} token={idToken}>
              <Dashboard idToken={idToken} user={user} />
            </RequireAuth>
          }
        />
        <Route
          path="/claims"
          element={
            <RequireAuth user={user} token={idToken}>
              <ClaimsList idToken={idToken} />
            </RequireAuth>
          }
        />
        <Route
          path="/claims/:claimNumber"
          element={
            <RequireAuth user={user} token={idToken}>
              <ClaimDetails idToken={idToken} />
            </RequireAuth>
          }
        />
      </Routes>
    </>
  );
}

export default App;
