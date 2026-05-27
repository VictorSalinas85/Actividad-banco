/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        // Wolfstreet Bank — paleta principal
        navy: {
          50:  '#eef2f8',
          100: '#d5dfee',
          200: '#aabedc',
          300: '#7493c4',
          400: '#476ca8',
          500: '#2d4f88',
          600: '#1f3a6b',
          700: '#152a50',
          800: '#0e1d3a',
          900: '#0a1428',
          950: '#050a18',
        },
        gold: {
          50:  '#fdf8ec',
          100: '#faebc6',
          200: '#f5d78a',
          300: '#efbf52',
          400: '#e5b662',
          500: '#d4a24e',
          600: '#b8852e',
          700: '#92651f',
          800: '#714d18',
          900: '#523710',
        },
        // Alias para compatibilidad con código existente
        primary: {
          50:  '#eef2f8',
          100: '#d5dfee',
          600: '#1f3a6b',
          700: '#152a50',
          800: '#0e1d3a',
          900: '#0a1428',
        },
        ink: {
          50:  '#f8fafc',
          100: '#f1f5f9',
          200: '#e2e8f0',
          300: '#cbd5e1',
          400: '#94a3b8',
          500: '#64748b',
          600: '#475569',
          700: '#334155',
          800: '#1e293b',
          900: '#0f172a',
        },
      },
      fontFamily: {
        brand: ['"Segoe UI"', 'Inter', 'system-ui', 'sans-serif'],
      },
      boxShadow: {
        'gold': '0 0 0 1px rgba(212, 162, 78, 0.4), 0 4px 16px -4px rgba(212, 162, 78, 0.25)',
        'navy-lg': '0 10px 30px -8px rgba(10, 20, 40, 0.45)',
      },
      backgroundImage: {
        'navy-gradient': 'linear-gradient(135deg, #0a1428 0%, #152a50 55%, #1f3a6b 100%)',
        'gold-gradient': 'linear-gradient(135deg, #d4a24e 0%, #e5b662 50%, #f5d78a 100%)',
      },
    },
  },
  plugins: [],
}
