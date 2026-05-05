import React, { useEffect, useState } from 'react';
import api from '../api';

const AdminDashboard = () => {
    const [dashboardData, setDashboardData] = useState(null);
    const [eventsData, setEventsData] = useState([]);

    useEffect(() => {
        api.get('/admin/analytics/dashboard')
            .then(res => setDashboardData(res.data))
            .catch(err => console.error(err));

        api.get('/admin/analytics/events')
            .then(res => setEventsData(res.data))
            .catch(err => console.error(err));
    }, []);

    if (!dashboardData) return <div className="text-center p-8">Loading Dashboard... (Ensure you are logged in as Admin)</div>;

    return (
        <div className="max-w-6xl mx-auto p-4">
            <h2 className="text-3xl font-bold mb-6 text-gray-800">Admin Dashboard</h2>
            <div className="grid grid-cols-2 md:grid-cols-5 gap-4 mb-8">
                <div className="bg-indigo-600 text-white p-4 rounded-lg shadow text-center">
                    <p className="text-sm font-semibold">Total Revenue</p>
                    <p className="text-2xl font-bold">${dashboardData.totalRevenue}</p>
                </div>
                <div className="bg-blue-500 text-white p-4 rounded-lg shadow text-center">
                    <p className="text-sm font-semibold">Total Registrations</p>
                    <p className="text-2xl font-bold">{dashboardData.totalRegistrations}</p>
                </div>
                <div className="bg-green-500 text-white p-4 rounded-lg shadow text-center">
                    <p className="text-sm font-semibold">Total Check-Ins</p>
                    <p className="text-2xl font-bold">{dashboardData.totalCheckIns}</p>
                </div>
                <div className="bg-orange-500 text-white p-4 rounded-lg shadow text-center">
                    <p className="text-sm font-semibold">Total Events</p>
                    <p className="text-2xl font-bold">{dashboardData.totalEvents}</p>
                </div>
                <div className="bg-purple-500 text-white p-4 rounded-lg shadow text-center">
                    <p className="text-sm font-semibold">Active Events</p>
                    <p className="text-2xl font-bold">{dashboardData.activeEvents}</p>
                </div>
            </div>

            <h3 className="text-2xl font-bold mb-4 text-gray-800">Event Analytics</h3>
            <div className="bg-white rounded-lg shadow overflow-hidden">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Event ID</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Registrations</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Check-Ins</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Revenue</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {eventsData.map((event, i) => (
                            <tr key={i}>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{event.eventId || event.id}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{event.totalRegistrations}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{event.totalCheckIns}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${event.totalRevenue}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                {eventsData.length === 0 && <p className="text-gray-500 p-4">No events found.</p>}
            </div>
        </div>
    );
};

export default AdminDashboard;
