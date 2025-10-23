import React from "react";
import CardContainer from "../CardContainer/CardContainer";
import Box from "@mui/material/Box";
import RecentClaimsCard from "../RecentClaimsCard/RecentClaimsCard";
import LinearProgress from "@mui/material/LinearProgress";

export default function DashboardContainer({ dashboardData }) {
  const activePlan = dashboardData?.activePlan;
  if (!activePlan) return null;

  const percent = Math.round((dashboardData.accumulators[0].usedAmount / dashboardData.accumulators[0].limitAmount) * 100);

  return (
    <Box
      display="flex"
      flexDirection="row"
      alignItems="flex-start"
      justifyContent="center"
    >
      <CardContainer title="Active Plan">
        <div style={{ display: "flex", flexDirection: "column", gap: "12px" }}>
          <strong>Name:</strong> {activePlan.name}
          <br />
          <strong>Network:</strong> {activePlan.networkName}
          <br />
          <strong>Plan Year:</strong> {activePlan.planYear}
          <br />
          <strong>Coverage:</strong> {activePlan.coverageStart} -{" "}
          {activePlan.coverageEnd}
        </div>
      </CardContainer>

      <CardContainer title="Accumulators">
        {dashboardData.accumulators.map((accumulator, index) => (
          <div
            key={index}
            style={{ display: "flex", flexDirection: "column", gap: "12px" }}
          >
            <strong>Type:</strong> {accumulator.type}
            <br />
            <strong>Tier:</strong> {accumulator.tier}
            <br />
            <strong>Used Amount:</strong> {accumulator.usedAmount}
            <br />
            <strong>Limit Amount:</strong> {accumulator.limitAmount}
            <br />
            <LinearProgress variant="determinate" value={percent} />
            <span style={{ alignSelf: "flex-end" }}>{percent}% used</span>
            <hr />
          </div>
        ))}
      </CardContainer>

      <RecentClaimsCard claims={dashboardData.recentClaims} />
    </Box>
  );
}
