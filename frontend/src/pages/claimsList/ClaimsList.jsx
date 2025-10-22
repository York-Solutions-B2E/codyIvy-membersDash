import React from "react";
import { useEffect, useState } from "react";

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
  return <div>Claims List</div>;
}
