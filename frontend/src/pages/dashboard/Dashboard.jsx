import React from 'react'

export default function Dashboard() {
  const [dashboardData, setDashboardData] = React.useState(null);


  useEffect(() => {
  fetch("/api/dashboard", {
    headers: {
      Authorization: `Bearer ${idToken}`
    }
  })
    .then(res => res.json())
    .then(data => {
      setDashboardData(data)
      console.log("Dashboard data:", data);
    })
    .catch(err => {
      console.error("Error fetching dashboard data:", err);
    });
}, [idToken]);

  return (
    <div>
      <h1>dashboard</h1>
    </div>
  )
}
