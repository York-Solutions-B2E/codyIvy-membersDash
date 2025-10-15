import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { BrowserRouter as Router } from "react-router-dom";

const clientId =
  "972231826627-m8prcdhvug72smfskigpnq667u2inp9k.apps.googleusercontent.com";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <GoogleOAuthProvider clientId={clientId}>
      <Router>
        <App />
      </Router>
    </GoogleOAuthProvider>
  </StrictMode>
);
