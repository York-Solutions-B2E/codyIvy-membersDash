import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";


export default function NavBar({ user, onSignOut }) {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Members Benefits Dashboard
          </Typography>

          <Typography
            variant="h6"
            component="div"
            sx={{
              flexGrow: 1,
              display: "flex",
              alignItems: "center",
              gap: 1, // adds spacing between text and avatar
            }}
          >
            {user ? `Welcome, ${user.firstName} ${user.lastName}` : "Not signed in"}
            
          </Typography>

          {user && (
            <Button color="inherit" onClick={onSignOut}>
              Sign Out
            </Button>
          )}
        </Toolbar>
      </AppBar>
    </Box>
  );
}
