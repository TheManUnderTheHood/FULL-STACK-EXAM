// registration page
import React, { useEffect, useState } from 'react';
import api from '../api';

const MyRegistrations = () => {
    const [registrations, setRegistrations] = useState([]);

    useEffect(() => {
        api.get('/registrations/my-registrations').then(response => {
            setRegistrations(response.data);
        }).catch(err => console.error(err));
    }, []);

    return (
        <div className="max-w-4xl mx-auto p-4">
            <h2 className="text-3xl font-bold mb-6 text-gray-800">My Registrations</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {registrations.map(reg => (
                    <div key={reg.id} className="bg-white p-6 rounded-lg shadow-md border border-gray-200">
                        <h3 className="text-xl font-bold mb-2 text-indigo-600">Registration #{reg.id}</h3>
                        <p className="text-gray-600 mb-2"><strong>Event ID:</strong> {reg.eventId}</p>
                        <p className="text-gray-600 mb-2"><strong>Status:</strong> {reg.paymentStatus}</p>
                        <p className="text-gray-600 mb-2"><strong>Check-In Status:</strong> {reg.checkedIn ? 'Yes' : 'No'}</p>
                        {reg.qrCodeData && (
                            <div className="mt-4 break-all bg-gray-100 p-2 rounded text-xs text-center border">
                                <p className="mb-2 font-bold text-gray-700">QR Token (Simulation):</p>
                                {reg.qrCodeData}
                            </div>
                        )}
                    </div>
                ))}
            </div>
            {registrations.length === 0 && <p className="text-gray-500">No registrations found.</p>}
        </div>
    );
};

export default MyRegistrations;
//end