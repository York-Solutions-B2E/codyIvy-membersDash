import React from 'react'
import { Navigate, useLocation } from 'react-router-dom';

export default function RequireAuth({ user, token, children }) {
  const location = useLocation();


  if (!user && !token) {
    return <Navigate to="/" state={{ from: location }} replace />;
  }

  return children
}
