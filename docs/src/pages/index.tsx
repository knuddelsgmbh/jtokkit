import React from 'react';
import Layout from '@theme/Layout';
import Link from '@docusaurus/Link';
import styles from './index.module.css';

export default function Home(): JSX.Element {
	return (
		<Layout>
			<main className={styles.intro}>
				<div className={'text--center'}>
					<div className={`padding--md text--bold ${styles.introText}`}>JTokkit</div>
					<div className={`padding--md ${styles.introSubtext}`}>A Java tokenizer library designed for use with OpenAI models</div>
					<div className={styles.buttonIntroContainer}>
						<Link
							to="/docs/getting-started"
							className={`button ${styles.buttonIntro} button--lg`}
						>
							Get Started
						</Link>
					</div>
				</div>
			</main>
		</Layout>
	);
}
