"use strict";(self.webpackChunkdocs=self.webpackChunkdocs||[]).push([[103],{3905:(e,n,t)=>{t.d(n,{Zo:()=>l,kt:()=>m});var o=t(7294);function a(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function i(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);n&&(o=o.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,o)}return t}function r(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?i(Object(t),!0).forEach((function(n){a(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):i(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function d(e,n){if(null==e)return{};var t,o,a=function(e,n){if(null==e)return{};var t,o,a={},i=Object.keys(e);for(o=0;o<i.length;o++)t=i[o],n.indexOf(t)>=0||(a[t]=e[t]);return a}(e,n);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(o=0;o<i.length;o++)t=i[o],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(a[t]=e[t])}return a}var c=o.createContext({}),s=function(e){var n=o.useContext(c),t=n;return e&&(t="function"==typeof e?e(n):r(r({},n),e)),t},l=function(e){var n=s(e.components);return o.createElement(c.Provider,{value:n},e.children)},g="mdxType",u={inlineCode:"code",wrapper:function(e){var n=e.children;return o.createElement(o.Fragment,{},n)}},p=o.forwardRef((function(e,n){var t=e.components,a=e.mdxType,i=e.originalType,c=e.parentName,l=d(e,["components","mdxType","originalType","parentName"]),g=s(t),p=a,m=g["".concat(c,".").concat(p)]||g[p]||u[p]||i;return t?o.createElement(m,r(r({ref:n},l),{},{components:t})):o.createElement(m,r({ref:n},l))}));function m(e,n){var t=arguments,a=n&&n.mdxType;if("string"==typeof e||a){var i=t.length,r=new Array(i);r[0]=p;var d={};for(var c in n)hasOwnProperty.call(n,c)&&(d[c]=n[c]);d.originalType=e,d[g]="string"==typeof e?e:a,r[1]=d;for(var s=2;s<i;s++)r[s]=t[s];return o.createElement.apply(null,r)}return o.createElement.apply(null,t)}p.displayName="MDXCreateElement"},5125:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>c,contentTitle:()=>r,default:()=>u,frontMatter:()=>i,metadata:()=>d,toc:()=>s});var o=t(7462),a=(t(7294),t(3905));const i={title:"Usage",sidebar_position:2},r=void 0,d={unversionedId:"getting-started/usage",id:"getting-started/usage",title:"Usage",description:"To use JTokkit, first create a new EncodingRegistry:",source:"@site/docs/getting-started/usage.md",sourceDirName:"getting-started",slug:"/getting-started/usage",permalink:"/docs/getting-started/usage",draft:!1,editUrl:"https://github.com/knuddelsgmbh/jtokkit/tree/main/docs/docs/getting-started/usage.md",tags:[],version:"current",sidebarPosition:2,frontMatter:{title:"Usage",sidebar_position:2},sidebar:"gettingStarted",previous:{title:"Introduction",permalink:"/docs/getting-started/"},next:{title:"Extending JTokkit",permalink:"/docs/getting-started/extending"}},c={},s=[{value:"Getting an encoding from the registry",id:"getting-an-encoding-from-the-registry",level:2},{value:"Encoding and decoding text",id:"encoding-and-decoding-text",level:2},{value:"Counting tokens",id:"counting-tokens",level:2},{value:"Encoding text with truncation",id:"encoding-text-with-truncation",level:2}],l={toc:s},g="wrapper";function u(e){let{components:n,...t}=e;return(0,a.kt)(g,(0,o.Z)({},l,t,{components:n,mdxType:"MDXLayout"}),(0,a.kt)("p",null,"To use JTokkit, first create a new ",(0,a.kt)("inlineCode",{parentName:"p"},"EncodingRegistry"),":"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-java"},"EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();\n")),(0,a.kt)("p",null,"Make sure to keep a reference to the registry, as the creation of the registry is expensive. Creating the registry loads the vocabularies from the classpath. The registry itself handles caching of the loaded encodings. It is thread-safe and can safely be used concurrently by multiple components."),(0,a.kt)("p",null,"If you do not want to automatically load all vocabularies of all encodings on registry creation, you can use the following lazy loading registry."),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-java"},"EncodingRegistry registry = Encodings.newLazyEncodingRegistry();\n")),(0,a.kt)("p",null,"This encoding registry only loads the vocabularies from encodings that are actually accessed. Vocabularies are only\nloaded once on first accessed. As with the default encoding registry, make sure to keep a reference to the registry\nto make use of the in-built caching of the vocabularies. It is thread-safe and can safely be used concurrently by\nmultiple components."),(0,a.kt)("h2",{id:"getting-an-encoding-from-the-registry"},"Getting an encoding from the registry"),(0,a.kt)("p",null,"You can use the registry to get the encodings you need:"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-java"},'// Get encoding via type-safe enum\nEncoding encoding = registry.getEncoding(EncodingType.CL100K_BASE);\n\n// Get encoding via string name\nOptional<Encoding> encoding = registry.getEncoding("cl100k_base");\n\n// Get encoding for a specific model via type-safe enum\nEncoding encoding = registry.getEncodingForModel(ModelType.GPT_4);\n\n// Get encoding for a specific model via string name\nOptional<Encoding> encoding = registry.getEncodingForModel("gpt_4");\n')),(0,a.kt)("h2",{id:"encoding-and-decoding-text"},"Encoding and decoding text"),(0,a.kt)("p",null,"You can use an ",(0,a.kt)("inlineCode",{parentName:"p"},"Encoding")," to encode and decode text:"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-java"},'IntArrayList encoded = encoding.encode("This is a sample sentence.");\n// encoded = [2028, 374, 264, 6205, 11914, 13]\n\nString decoded = encoding.decode(encoded);\n// decoded = "This is a sample sentence."\n')),(0,a.kt)("p",null,"The encoding is also fully thread-safe and can be used concurrently by multiple components."),(0,a.kt)("admonition",{type:"info"},(0,a.kt)("p",{parentName:"admonition"},"Note that the library does not support encoding of special tokens. Special tokens are artificial tokens used to unlock capabilities from a model, such as fill-in-the-middle. If the ",(0,a.kt)("inlineCode",{parentName:"p"},"Encoding#encode")," method encounters a special token in the input text, it will throw an ",(0,a.kt)("inlineCode",{parentName:"p"},"UnsupportedOperationException"),"."),(0,a.kt)("p",{parentName:"admonition"},"If you want to encode special tokens as if they were normal text, you can use ",(0,a.kt)("inlineCode",{parentName:"p"},"Encoding#encodeOrdinary")," instead:"),(0,a.kt)("pre",{parentName:"admonition"},(0,a.kt)("code",{parentName:"pre",className:"language-java"},'encoding.encode("hello <|endoftext|> world");\n// raises an UnsupportedOperationException\n\nencoding.encodeOrdinary("hello <|endoftext|> world");\n// returns [15339, 83739, 8862, 728, 428, 91, 29, 1917]\n'))),(0,a.kt)("h2",{id:"counting-tokens"},"Counting tokens"),(0,a.kt)("p",null,"If all you want is the amount of tokens the text encodes to, you can use the shorthand method ",(0,a.kt)("inlineCode",{parentName:"p"},"Encoding#countTokens")," or ",(0,a.kt)("inlineCode",{parentName:"p"},"Encoding#countTokensOrdinary"),". These methods are faster than the corresponding ",(0,a.kt)("inlineCode",{parentName:"p"},"encode")," methods."),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-java"},'int tokenCount = encoding.countTokens("This is a sample sentence.");\n// tokenCount = 6\n\nint tokenCount = encoding.countTokensOrdinary("hello <|endoftext|> world");\n// tokenCount = 8\n')),(0,a.kt)("h2",{id:"encoding-text-with-truncation"},"Encoding text with truncation"),(0,a.kt)("p",null,"If you want to only encode up until a specified amount of ",(0,a.kt)("inlineCode",{parentName:"p"},"maxTokens")," and truncate after that amount, you can use ",(0,a.kt)("inlineCode",{parentName:"p"},"Encoding#encode(String, int)")," or ",(0,a.kt)("inlineCode",{parentName:"p"},"Encoding#encodeOrdinary(String, int)"),". These methods will truncate the encoded tokens to the specified length. They will automatically handle unicode characters that were split in half by the truncation by removing those tokens from the end of the list."),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-java"},'IntArrayList encoded = encoding.encode("This is a sample sentence.", 3);\n// encoded = [2028, 374, 264]\n\nString decoded = encoding.decode(encoded);\n// decoded = "This is a"\n\nIntArrayList encoded = encoding.encode("I love \ud83c\udf55", 4);\n// encoded = [40, 3021]\n\nString decoded = encoding.decode(encoded);\n// decoded = "I love"\n')))}u.isMDXComponent=!0}}]);