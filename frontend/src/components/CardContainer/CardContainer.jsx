import React from "react";
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';

function CardContainer({ title, children, sx }) {
  return (
    <Card sx={{ maxWidth: 600, margin: "1rem auto", ...sx }}>
      {title && <CardHeader title={title} />}
      <CardContent>
        {children}
      </CardContent>
    </Card>
  );
}

export default CardContainer;   