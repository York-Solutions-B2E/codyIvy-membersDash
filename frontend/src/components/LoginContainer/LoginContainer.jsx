import React from "react";
import { Box, Button, Paper, Typography } from "@mui/material";
import GoogleIcon from "@mui/icons-material/Google";
import { useGoogleLogin } from "@react-oauth/google";

export default function LoginContainer({ onLogin }) {
  const [errorMsg, setErrorMsg] = React.useState(null);

  const handleLogin = async (code) => {
    try {
      const response = await fetch("http://localhost:8080/api/auth/google", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ code }),
      });

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      console.log("Server response:", data);
      setErrorMsg(null);

      const {user, tokens} = data;

      if (onLogin) {
        onLogin(data.user_info ); // Pass user info
      }
      
    } catch (error) {
      console.error("Login failed:", error);
      setErrorMsg("Login failed. Please try again.");
    }
  };

  const login = useGoogleLogin({
    flow: "auth-code",
    onSuccess: (tokenResponse) => {
      setErrorMsg(null);
      handleLogin(tokenResponse.code);
    },
    onError: (errorResponse) => {
      setErrorMsg("Login failed. Please try again.");
      console.log("google login error!", errorResponse);
    },
    scope: "openid profile email",
  });

  return (
    <Box
      sx={{
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        bgcolor: "background.default",
      }}
    >
      <Paper
        elevation={6}
        sx={{
          p: 4,
          borderRadius: 3,
          textAlign: "center",
          minWidth: 300,
        }}
      >
        <Typography variant="h5" gutterBottom>
          Welcome
        </Typography>
        <Button
          variant="contained"
          color="primary"
          startIcon={<GoogleIcon />}
          onClick={login}
          sx={{
            textTransform: "none",
            fontWeight: 500,
            bgcolor: "#4285F4",
            "&:hover": { bgcolor: "#357AE8" },
          }}
        >
          Sign in with Google
        </Button>
        {errorMsg && (
          <Typography color="error" sx={{ mt: 2 }}>
            {errorMsg}
          </Typography>
        )}
      </Paper>
    </Box>
  );
}
