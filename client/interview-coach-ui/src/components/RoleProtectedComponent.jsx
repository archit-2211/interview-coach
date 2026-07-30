import React from 'react'
import {Navigate,Outlet} from "react-router-dom";
import { getRole } from '../services/AuthService';
import RestrictedAccess from './RestrictedAccess';


function RoleProtectedComponent({allowedRoles = []}) {
    const role = getRole()

    
    
    return (
        <>
        {allowedRoles.includes(role) ? <Outlet/> : <RestrictedAccess/> }
    
        
        </>
  
    )
}

export default RoleProtectedComponent