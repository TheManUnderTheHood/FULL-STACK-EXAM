import React, { useEffect, useState } from 'react';
import api from '../api';

const EventList = () => {
    const [events, setEvents] = useState([]);

    useEffect(() => {
        api.get('/events').then(response => {
            setEvents(response.data);
        }).catch(err => console.error(err));
    }, []);

    const handleRegister = async (eventId) => {
        try {
            await api.post(`/registrations/register/${eventId}`);
            alert('Successfully registered!');
        } catch (err) {
            console.error(err);
            alert(err.response?.data || 'Failed to register');
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-4">
            <h2 className="text-3xl font-bold mb-6 text-gray-800">Upcoming Events</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {events.map(event => (
                    <div key={event.id} className="bg-white p-6 rounded-lg shadow-md border border-gray-200 hover:shadow-lg transition-shadow">
                        <h3 className="text-xl font-bold mb-2 text-indigo-600">{event.title}</h3>
                        <p className="text-gray-600 mb-4">{event.description}</p>
                        <div className="flex flex-col space-y-2 text-sm text-gray-500">
                            <span><strong>Date:</strong> {new Date(event.startDate).toLocaleDateString()}</span>
                            <span><strong>Location:</strong> {event.location}</span>
                            <span><strong>Price:</strong> ${event.price}</span>
                        </div>
                        <button
                            onClick={() => handleRegister(event.id)}
                            className="mt-4 w-full bg-indigo-600 text-white py-2 rounded hover:bg-indigo-700 transition"
                        >
                            Register
                        </button>
                    </div>
                ))}
            </div>
            {events.length === 0 && <p className="text-gray-500">No events found.</p>}
        </div>
    );
};

export default EventList;
