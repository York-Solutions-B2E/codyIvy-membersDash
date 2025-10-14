import React from "react";
import { Box, Button, Paper, Typography } from "@mui/material";
import GoogleIcon from "@mui/icons-material/Google";

export default function LoginContainer({ onLogin }) {
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
        <Typography variant="body1" sx={{ mb: 3 }}>
          Sign in to continue
        </Typography>
        <Button
          variant="contained"
          color="primary"
          startIcon={<GoogleIcon />}
          onClick={onLogin}
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
