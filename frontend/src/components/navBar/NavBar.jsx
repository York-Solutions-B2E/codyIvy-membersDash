import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import { NavLink } from "react-router-dom";

export default function NavBar({ user, onSignOut }) {
  return (
    <Box sx={{ flexGrow: 1, }}>
      <AppBar position="static">
        <Toolbar >
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Members Benefits
          </Typography>
          <Box
            sx={{
              display: "flex",
              gap: 2,
            }}
          >
            <Button
              component={NavLink}
              to="/dashboard"
              sx={{
                color: "inherit",
                fontWeight: 600,
                textTransform: "none",
                borderBottom: ({ isActive }) =>
                  isActive ? "2px solid #1976d2" : "none",
              }}
              // NavLink gives "isActive" prop to sx!
            >
              Dashboard
            </Button>
            <Button
              component={NavLink}
              to="/claims"
              sx={{
                color: "inherit",
                fontWeight: 600,
                textTransform: "none",
                borderBottom: ({ isActive }) =>
                  isActive ? "2px solid #1976d2" : "none",
              }}
            >
              Claims
            </Button>
          </Box>
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
            {user ? `Welcome, ${user.name}` : "Not signed in"}
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
