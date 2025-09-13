// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const { themes } = require('prism-react-renderer');
const lightCodeTheme = themes.github;
const darkCodeTheme = themes.dracula;

/** @type {import('@docusaurus/types').Config} */
const config = {
	title: 'JTokkit',
	tagline: 'A Java tokenizer library designed for use with OpenAI models',
	favicon: 'img/favicon.ico',

	url: 'https://jtokkit.knuddels.de',
	baseUrl: '/',

	organizationName: 'knuddelsgmbh',
	projectName: 'jtokkit',
	deploymentBranch: 'gh-pages',
	trailingSlash: false,

	onBrokenLinks: 'throw',
	onBrokenMarkdownLinks: 'warn',

	i18n: {
		defaultLocale: 'en',
		locales: ['en'],
	},

	presets: [
		[
			'classic',
			/** @type {import('@docusaurus/preset-classic').Options} */
			({
				docs: {
					sidebarPath: require.resolve('./sidebars.js'),
					editUrl: 'https://github.com/knuddelsgmbh/jtokkit/tree/main/docs',
				},
				theme: {
					customCss: require.resolve('./src/css/custom.css'),
				},
			}),
		],
	],

	plugins: [
		require.resolve('docusaurus-lunr-search'),
	],

	themeConfig:
		/** @type {import('@docusaurus/preset-classic').ThemeConfig} */
		({
			colorMode: {
				defaultMode: 'dark',
				respectPrefersColorScheme: true,
			},
			navbar: {
				title: 'JTokkit',
				logo: {
					alt: 'Knuddels Logo',
					src: 'img/logo.png',
				},
				items: [
					{
						type: 'docSidebar',
						sidebarId: 'gettingStarted',
						position: 'left',
						label: 'Getting Started',
					},
					{
						href: 'https://javadoc.io/doc/com.knuddels/jtokkit/latest/index.html',
						label: 'JavaDoc',
						position: 'left',
					},
					{
						href: 'https://jobs.knuddels.de',
						label: 'Jobs',
						position: 'right',
					},
					{
						href: 'https://github.com/knuddelsgmbh/jtokkit',
						label: 'GitHub',
						position: 'right',
					},
					{
						type: 'search',
						position: 'right',
					},
				],
			},
			footer: {
				style: 'dark',
				copyright: `Copyright © ${new Date().getFullYear()} Knuddels`,
			},
			prism: {
				theme: lightCodeTheme,
				darkTheme: darkCodeTheme,
				additionalLanguages: ['java'],
			},
		}),
};

module.exports = config;
