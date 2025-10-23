import React from "react";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import {
  Paper,
  Typography,
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Stack,
  Button,
} from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";

export default function ClaimDetails() {
  const { claimNumber } = useParams();
  const [claimData, setClaimData] = React.useState(null);
  const [loading, setLoading] = React.useState(true);
  const navigate = useNavigate();

  React.useEffect(() => {
    if (!claimNumber) return;

    fetch(`/api/claims/${claimNumber}`, {
      headers: {
        Authorization: `Bearer ${localStorage.getItem("idToken")}`,
      },
    })
      .then((response) => {
        if (!response.ok) throw new Error("Network response was not ok");
        return response.json();
      })
      .then((data) => {
        setClaimData(data);
      })
      .catch((error) => {
        console.error("Failed to fetch claim details:", error);
      })
      .finally(() => setLoading(false));
  }, [claimNumber]);

  if (loading) return <div>Loading...</div>;

  if (!claimData) return <div>No claim data found.</div>;
  return (
    <Box p={3} maxWidth={1200} mx="auto">
      <Paper elevation={1} sx={{ mb: 3 }}>
        {/* Header Section */}
        <Box p={2} borderBottom="1px solid #eee">
          <Stack
            direction="row"
            justifyContent="space-between"
            alignItems="center"
            mb={1}
          >
            <Typography variant="h5">Claim #{claimData.claimNumber}</Typography>
            <Stack direction="row" spacing={2} alignItems="center">
              <Typography variant="body1">
                Provider: {claimData.provider?.name}
              </Typography>
              <Typography variant="body1">
                Service: {claimData.serviceStartDate}
                {claimData.serviceStartDate !== claimData.serviceEndDate &&
                  `–${claimData.serviceEndDate}`}
              </Typography>
            </Stack>
          </Stack>
          <Stack direction="row" alignItems="center" spacing={1}>
            <Typography variant="body1">Status: {claimData.status}</Typography>
            <Typography variant="body2" color="textSecondary">
              [Submitted]—[In Review]—[Processed]—[Paid]
            </Typography>
          </Stack>
        </Box>

        {/* Financial Summary */}
        <Box p={2} borderBottom="1px solid #eee">
          <Typography variant="h6" mb={2}>
            Financial Summary
          </Typography>
          <Stack spacing={1}>
            <Typography>
              • Total Billed: $
              {claimData.financialSummary?.totalBilled?.toFixed(2)}
            </Typography>
            <Typography>
              • Allowed Amount: $
              {claimData.financialSummary?.totalAllowed?.toFixed(2)}
            </Typography>
            <Typography>
              • Plan Paid: ${claimData.financialSummary?.totalPaid?.toFixed(2)}
            </Typography>
            <Typography>
              • Member Responsibility: $
              {claimData.financialSummary?.memberResponsibility?.toFixed(2)}
            </Typography>
          </Stack>
        </Box>

        {/* Line Items */}
        <Box p={2} borderBottom="1px solid #eee">
          <Typography variant="h6" mb={2}>
            Line Items
          </Typography>
          <TableContainer>
            <Table size="small">
              <TableHead>
                <TableRow>
                  <TableCell>CPT</TableCell>
                  <TableCell>Description</TableCell>
                  <TableCell align="right">Billed</TableCell>
                  <TableCell align="right">Allowed</TableCell>
                  <TableCell align="right">Ded</TableCell>
                  <TableCell align="right">Copay</TableCell>
                  <TableCell align="right">Coins</TableCell>
                  <TableCell align="right">You</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {claimData.lineItems?.map((line) => (
                  <TableRow key={line.lineNumber}>
                    <TableCell>{line.cptCode}</TableCell>
                    <TableCell>{line.description}</TableCell>
                    <TableCell align="right">
                      {line.billedAmount?.toFixed(2)}
                    </TableCell>
                    <TableCell align="right">
                      {line.allowedAmount?.toFixed(2)}
                    </TableCell>
                    <TableCell align="right">
                      {line.deductibleApplied?.toFixed(2)}
                    </TableCell>
                    <TableCell align="right">
                      {line.copayApplied?.toFixed(2)}
                    </TableCell>
                    <TableCell align="right">
                      {line.coinsuranceApplied?.toFixed(2)}
                    </TableCell>
                    <TableCell align="right">
                      {line.memberResponsibility?.toFixed(2)}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Box>

        {/* Footer Actions */}
        <Box p={2}>
          <Stack
            direction="row"
            justifyContent="space-between"
            alignItems="center"
          >
            <Button
              startIcon={<ArrowBackIcon />}
              onClick={() => navigate("/claims")}
              variant="outlined"
            >
              Back to Claims
            </Button>
          </Stack>
        </Box>
      </Paper>
    </Box>
  );
}
