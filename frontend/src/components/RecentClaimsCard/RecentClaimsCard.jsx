import React from "react";
import { useNavigate } from "react-router-dom";
import CardContainer from "../CardContainer/CardContainer";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";

function RecentClaimsCard({ claims }) {
  const navigate = useNavigate();

  if (!claims || claims.length === 0) {
    return (
      <CardContainer title="Recent Claims">
        <div>No recent claims to display.</div>
      </CardContainer>
    );
  }

  const handleClaimClick = (claimNumber) => {
    navigate(`/claims/${claimNumber}`);
  };

  return (
    <CardContainer title="Recent Claims">
      <Table size="small">
        <TableHead>
          <TableRow>
            <TableCell>Claim #</TableCell>
            <TableCell>Status</TableCell>
            <TableCell align="right">Member Responsibility</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {claims.map((claim, idx) => (
            <TableRow
              key={idx}
              onClick={() => handleClaimClick(claim.claimNumber)}
              sx={{
                cursor: "pointer",
                "&:hover": { backgroundColor: "#f5f5f5" },
              }}
            >
              <TableCell>{claim.claimNumber}</TableCell>
              <TableCell>{claim.status}</TableCell>
              <TableCell align="right">
                ${claim.memberResponsibility.toFixed(2)}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </CardContainer>
  );
}

export default RecentClaimsCard;
