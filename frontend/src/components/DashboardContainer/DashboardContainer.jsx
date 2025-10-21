import React from "react";
import CardContainer from "../CardContainer/CardContainer";
import Box from "@mui/material/Box";

export default function DashboardContainer({ dashboardData }) {
  const activePlan = dashboardData?.activePlan;
  if (!activePlan) return null;
  console.log("Active Plan:", dashboardData);

  return (
    <Box
   
      display="flex"
      flexDirection="row"
      alignItems="flex-start"
      justifyContent="center"
    >
      <CardContainer title="Active Plan">
        <div>
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
          <div key={index}>
            <strong>Type:</strong> {accumulator.type}
            <br />
            <strong>Tier:</strong> {accumulator.tier}
            <br />
            <strong>Used Amount:</strong> {accumulator.usedAmount}
            <br />
            <strong>Limit Amount:</strong> {accumulator.limitAmount}
            <br />
          </div>
        ))}
      </CardContainer>
    </Box>
  );
}
