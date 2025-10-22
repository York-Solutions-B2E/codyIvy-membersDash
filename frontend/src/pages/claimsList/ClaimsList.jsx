import React from "react";
import { useEffect, useState } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
} from "@mui/material";

export default function ClaimsList({ idToken }) {
  const [claims, setClaims] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("/api/claims?page=0&size=10", {
      headers: {
        Authorization: `Bearer ${idToken}`,
      },
    })
      .then((response) => {
        if (!response.ok) throw new Error("Network response was not ok");
        return response.json();
      })
      .then((data) => {
        setClaims(data.content); // Spring Page<T> wraps results in .content
        console.log("Fetched claims:", data.content);
      })
      .catch((error) => {
        console.error("Failed to fetch claims:", error);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading...</div>;

  if (claims.length === 0) return <div>No claims found.</div>;
  return (
    <div>
      <h1>Claims List</h1>
      <TableContainer component={Paper} sx={{ mt: 2 }}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Claim #</TableCell>
              <TableCell>Service Dates</TableCell>
              <TableCell>Provider</TableCell>
              <TableCell>Status</TableCell>
              <TableCell align="right">Member Responsibility</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {claims.map((claim) => (
              <TableRow
                key={claim.claimNumber}
                sx={{
                  "&:last-child td, &:last-child th": { border: 0 },
                  borderBottom: "2px solid #eee",
                }}
              >
                <TableCell>{claim.claimNumber}</TableCell>
                <TableCell>
                  {claim.serviceStartDate}
                  {claim.serviceStartDate !== claim.serviceEndDate
                    ? ` – ${claim.serviceEndDate}`
                    : ""}
                </TableCell>
                <TableCell>{claim.providerName}</TableCell>
                <TableCell>{claim.status}</TableCell>
                <TableCell align="right">
                  ${claim.memberResponsibility?.toFixed(2)}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}
