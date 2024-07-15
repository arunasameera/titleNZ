import React from 'react';
import {render, screen} from '@testing-library/react';
import App from './App';

test('renders landing page', () => {
    render(<App/>);
    const landingPageText = screen.getByText(/Welcome to LandOnLite/i);
    expect(landingPageText).toBeInTheDocument();
});
