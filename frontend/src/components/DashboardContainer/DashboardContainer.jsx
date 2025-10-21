import React from "react";
import CardContainer from "../CardContainer/CardContainer";

export default function DashboardContainer({ dashboardData }) {
  const activePlan = dashboardData?.activePlan;
  if (!activePlan) return null;
  console.log("Active Plan:", dashboardData);

  return (
    <div>
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
    </div>
  );
}
