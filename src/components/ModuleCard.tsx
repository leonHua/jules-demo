import React from 'react';

interface ModuleCardProps {
  number: string;
  title: string;
  items: string[];
}

const ModuleCard: React.FC<ModuleCardProps> = ({ number, title, items }) => {
  return (
    <div className="bg-gray-800 bg-opacity-50 p-6 rounded-lg border border-gray-700">
      <div className="flex items-center mb-4">
        <div className="text-5xl font-bold text-blue-400 mr-4">{number}</div>
        <h2 className="text-2xl font-semibold">{title}</h2>
      </div>
      <ul className="text-left text-gray-300 space-y-2">
        {items.map((item, index) => (
          <li key={index}>{item}</li>
        ))}
      </ul>
    </div>
  );
};

export default ModuleCard;