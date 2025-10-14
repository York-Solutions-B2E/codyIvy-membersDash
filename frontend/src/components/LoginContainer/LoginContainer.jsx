import React from "react";
import { Box, Button, Paper, Typography } from "@mui/material";
import GoogleIcon from "@mui/icons-material/Google";
import { useGoogleLogin } from "@react-oauth/google";

export default function LoginContainer({ onLogin }) {
  const login = useGoogleLogin({
    onSuccess: (tokenResponse) => {
      console.log("google login success!", tokenResponse);
      if (onLogin) {
        onLogin(tokenResponse);
      }
    },
    onError: (errorResponse) =>
      console.log("google login error!", errorResponse),
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
      </Paper>
    </Box>
  );
}
