import React from "react";
import { useEffect } from "react";
import DashboardContainer from "../../components/DashboardContainer/DashboardContainer";
import Button from "@mui/material/Button";

export default function Dashboard({ idToken }) {
  const [dashboardData, setDashboardData] = React.useState(null);

  useEffect(() => {
    fetch("/api/dashboard", {
      headers: {
        Authorization: `Bearer ${idToken}`,
      },
    })
      .then(async (res) => await res.json())
      .then((data) => {
        setDashboardData(data);
        console.log("Dashboard data:", data);
      })
      .catch((err) => {
        console.error("Error fetching dashboard data:", err);
      });
  }, [idToken]);

  return (
    <div>
      <h1>Dashboard</h1>
      {dashboardData ? (
        <DashboardContainer dashboardData={dashboardData} />
      ) : (
        <div>Loading dashboard...</div>
      )}
      <Button variant="contained" color="primary">
        View All Claims
      </Button>
    </div>
  );
}
