import React from 'react';
import {render, screen} from '@testing-library/react';
import {TitlePage} from "./TitlePage";

test('renders landing page', () => {
    render(<TitlePage/>);
    const titleText = screen.getByText(/Title #/i);
    expect(titleText).toBeInTheDocument();
});
