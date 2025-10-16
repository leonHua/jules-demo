import React from 'react';

const ConnectingPath = () => {
  return (
    <svg
      className="absolute top-0 left-0 w-full h-full pointer-events-none"
      preserveAspectRatio="none"
      viewBox="0 0 1200 1000" // Adjusted viewBox for better aspect ratio
    >
      <style>
        {`
          .desktop-path, .tablet-path {
            display: none;
          }
          @media (min-width: 1024px) {
            .desktop-path { display: block; }
          }
          @media (min-width: 768px) and (max-width: 1023px) {
            .tablet-path { display: block; }
          }
        `}
      </style>
      <defs>
        <marker
          id="dot"
          viewBox="0 0 10 10"
          refX="5"
          refY="5"
          markerWidth="5"
          markerHeight="5"
        >
          <circle cx="5" cy="5" r="5" fill="#4A90E2" />
        </marker>
      </defs>

      {/* Path for large screens (3 columns) - A more accurate snake-like path */}
      <path
        className="desktop-path"
        d="M 200 250 C 400 250, 400 450, 600 450 S 800 250, 1000 250"
        stroke="#4A90E2"
        strokeWidth="2"
        fill="none"
        markerStart="url(#dot)"
        markerMid="url(#dot)"
        markerEnd="url(#dot)"
      />

      {/* Path for medium screens (2 columns) */}
      <path
        className="tablet-path"
        d="M 300 200 C 600 200, 600 400, 900 400 C 300 400, 300 600, 600 600"
        stroke="#4A90E2"
        strokeWidth="2"
        fill="none"
        markerStart="url(#dot)"
        markerMid="url(#dot)"
        markerEnd="url(#dot)"
      />
    </svg>
  );
};

export default ConnectingPath;