import React, { useState } from 'react';
import api from '../api';

const EventSetup = () => {
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [date, setDate] = useState('');
    const [location, setLocation] = useState('');
    const [price, setPrice] = useState(0);
    const [capacity, setCapacity] = useState(100);

    const handleCreateEvent = async (e) => {
        e.preventDefault();
        try {
            await api.post('/organizer/events', {
                title,
                description,
                startDate: new Date(date).toISOString(),
                endDate: new Date(date).toISOString(), // simplifying
                location,
                price: parseFloat(price),
                totalSeats: parseInt(capacity),
                availableSeats: parseInt(capacity),
                status: 'PUBLISHED'
            });
            alert('Event Created Successfully!');
            setTitle(''); setDescription(''); setDate(''); setLocation(''); setPrice(0); setCapacity(100);
        } catch (error) {
            console.error(error);
            alert('Failed to create event. Make sure you are an Admin/Organizer.');
        }
    };

    return (
        <div className="max-w-2xl mx-auto p-6 bg-white rounded-lg shadow mt-10">
            <h2 className="text-2xl font-bold mb-6 text-gray-800">Create New Event</h2>
            <form onSubmit={handleCreateEvent} className="space-y-4">
                <div>
                    <label className="block text-sm font-medium text-gray-700">Event Title</label>
                    <input type="text" required className="mt-1 block w-full p-2 border border-gray-300 rounded" value={title} onChange={e => setTitle(e.target.value)} />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">Description</label>
                    <textarea required className="mt-1 block w-full p-2 border border-gray-300 rounded" value={description} onChange={e => setDescription(e.target.value)}></textarea>
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">Date & Time</label>
                    <input type="datetime-local" required className="mt-1 block w-full p-2 border border-gray-300 rounded" value={date} onChange={e => setDate(e.target.value)} />
                </div>
                <div>
                    <label className="block text-sm font-medium text-gray-700">Location</label>
                    <input type="text" required className="mt-1 block w-full p-2 border border-gray-300 rounded" value={location} onChange={e => setLocation(e.target.value)} />
                </div>
                <div className="flex space-x-4">
                    <div className="flex-1">
                        <label className="block text-sm font-medium text-gray-700">Price ($)</label>
                        <input type="number" step="0.01" min="0" required className="mt-1 block w-full p-2 border border-gray-300 rounded" value={price} onChange={e => setPrice(e.target.value)} />
                    </div>
                    <div className="flex-1">
                        <label className="block text-sm font-medium text-gray-700">Total Capacity</label>
                        <input type="number" min="1" required className="mt-1 block w-full p-2 border border-gray-300 rounded" value={capacity} onChange={e => setCapacity(e.target.value)} />
                    </div>
                </div>
                <button type="submit" className="w-full bg-indigo-600 text-white font-bold py-2 rounded hover:bg-indigo-700">Create Event</button>
            </form>
        </div>
    );
};

export default EventSetup;
