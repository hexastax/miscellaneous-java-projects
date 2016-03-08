# job-runner
Simple configurable multi-threaded job runner implemented in Java.

## Notes
- A **job type** is characterized by a name, description, and the associated job implementation class which does the actual job task. Job types are defined in the **job-types.xml** configuration file.
- A **job** is defined via XML, as a name, description, type, and a set of 0 or more properties.
- A **job factory** is used to instantiate different job class implementations.

## Execution
To exercise the functionality, the JobRunnerCmd command-line utility can be used:

```
Usage:
com.hexastax.jobrunner.runner.JobRunnerCmd
 -?                       Print this message.
 -h                       Print this message.
 -help                    Print this message.
 -jobs <jobDefLoc>        Mandatory path to the job definitions directory.
 -nt <numThreads>         Optional number of threads to use, defaults to
                          5.
 -types <jobTypeDefLoc>   Mandatory path to the job type definitions XML
                          file.
```
## Sample invokation
```
java com.hexastax.jobrunner.runner.JobRunnerCmd -types ./conf/job-types.xml -jobs ./jobs -nt 3
```

The sample /jobs directory contains two simple jobs, one for concatenating together the values
of all the properties in the job definition, the other for reversing the order in which the
job definition property values occur and returning that as a result.

Sample execution trace:
```
>> Starting the Job Runner...
>> Fetching job definitions from: /projects/jobrunner/jobs...
>> Job fetching OK, #jobs: 2
>> Running the jobs...
>> Job Runner done.
    >> Job 'Reverse Order of Property Values'. Result : value4,value3,value2,value1
    >> Job 'Concatenate Property Values'. Result : value1-value2-value3-value4
>> All finished.
>> Execution time: 32 ms.
```
