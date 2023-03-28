#!/usr/bin/env python3
import pyperf

setup = """
import os
import tiktoken

file_contents = []
for filename in os.listdir("data"):
    if filename.endswith(".txt") == False:
        continue

    with open("data/" + filename, mode="r", encoding="utf-8") as f:
        file_contents.append(f.read())

cl100k_base = tiktoken.get_encoding("cl100k_base")
p50k_base = tiktoken.get_encoding("p50k_base")
p50k_edit = tiktoken.get_encoding("p50k_edit")
r50k_base = tiktoken.get_encoding("r50k_base")
"""

runner = pyperf.Runner()


def run_benchmark(name, num_threads):
    runner.timeit(
        name=f'{name} {num_threads} thread(s)',
        stmt=f'{name}.encode_ordinary_batch(file_contents, num_threads={num_threads})',
        setup=setup
    )


run_benchmark("cl100k_base", 1)
run_benchmark("cl100k_base", 2)
run_benchmark("cl100k_base", 4)
run_benchmark("cl100k_base", 8)
run_benchmark("cl100k_base", 16)
run_benchmark("cl100k_base", 32)
run_benchmark("cl100k_base", 64)

run_benchmark("p50k_base", 1)
run_benchmark("p50k_base", 2)
run_benchmark("p50k_base", 4)
run_benchmark("p50k_base", 8)
run_benchmark("p50k_base", 16)
run_benchmark("p50k_base", 32)
run_benchmark("p50k_base", 64)

run_benchmark("p50k_edit", 1)
run_benchmark("p50k_edit", 2)
run_benchmark("p50k_edit", 4)
run_benchmark("p50k_edit", 8)
run_benchmark("p50k_edit", 16)
run_benchmark("p50k_edit", 32)
run_benchmark("p50k_edit", 64)

run_benchmark("r50k_base", 1)
run_benchmark("r50k_base", 2)
run_benchmark("r50k_base", 4)
run_benchmark("r50k_base", 8)
run_benchmark("r50k_base", 16)
run_benchmark("r50k_base", 32)
run_benchmark("r50k_base", 64)
