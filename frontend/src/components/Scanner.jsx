import React, { useState } from 'react';
import { Html5QrcodeScanner } from 'html5-qrcode';
import api from '../api';

const Scanner = () => {
    const [scanResult, setScanResult] = useState(null);
    const [error, setError] = useState('');

    React.useEffect(() => {
        const scanner = new Html5QrcodeScanner('reader', {
            qrbox: { width: 250, height: 250 },
            fps: 5,
        });

        scanner.render(
            async (result) => {
                scanner.clear();
                try {
                    const response = await api.post('/volunteer/checkin/scan', { qrToken: result });
                    setScanResult(response.data);
                    setError('');
                } catch (err) {
                    setError('Invalid or previously scanned QR Code.');
                    setScanResult(null);
                }
            },
            (err) => {
                // Ignore scanning errors
            }
        );

        return () => {
            scanner.clear().catch(error => console.error("Failed to clear scanner", error));
        };
    }, []);

    return (
        <div className="max-w-xl mx-auto py-10">
            <h2 className="text-3xl font-bold mb-6 text-center text-gray-800">QR Scanner</h2>
            <div id="reader" className="w-full bg-white rounded-lg shadow overflow-hidden"></div>
            {scanResult && (
                <div className="mt-6 p-4 bg-green-100 text-green-800 rounded-lg">
                    <p className="font-bold text-lg">Check-In Successful!</p>
                    <p>Attendee has been verified.</p>
                </div>
            )}
            {error && (
                <div className="mt-6 p-4 bg-red-100 text-red-800 rounded-lg">
                    <p className="font-bold text-lg">Check-In Failed!</p>
                    <p>{error}</p>
                </div>
            )}
        </div>
    );
};

export default Scanner;
