import os
import tiktoken
import csv

encoding = tiktoken.get_encoding("o200k_base")

with open("../lib/src/test/resources/base_prompts.csv", mode="r", encoding="utf-8") as f:
    csvdata = csv.reader(f, delimiter=",", quotechar='"')
    next(csvdata, None)
    with open("../lib/src/test/resources/o200k_base_encodings.csv", mode="w", encoding="utf-8") as outFile:
        writer = csv.writer(outFile, delimiter=",", quotechar='"')
        writer.writerow(["input","output","outputMaxTokens10"])
        for row in csvdata:
            encoded = encoding.encode_ordinary(row[0])
            for i in reversed(range(11)):
                encodedShort = encoded[:i]
                decoded = encoding.decode(encodedShort)
                if (row[0].startswith(decoded)):
                    writer.writerow([row[0], encoded, encodedShort])
                    break

