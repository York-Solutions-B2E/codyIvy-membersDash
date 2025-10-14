import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { BrowserRouter as Router } from "react-router-dom";

const clientId =
  "558192641343-rs8diq8e51klpq0f5ap5rfgbku426l8r.apps.googleusercontent.com";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <GoogleOAuthProvider clientId={clientId}>
      <Router>
        <App />
      </Router>
    </GoogleOAuthProvider>
  </StrictMode>
);
