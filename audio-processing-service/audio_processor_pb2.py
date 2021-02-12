# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: audio_processor.proto
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='audio_processor.proto',
  package='audio_processor',
  syntax='proto3',
  serialized_options=None,
  create_key=_descriptor._internal_create_key,
  serialized_pb=b'\n\x15\x61udio_processor.proto\x12\x0f\x61udio_processor\"<\n\x0b\x41udioSample\x12\n\n\x02id\x18\x01 \x01(\x11\x12\x11\n\ttimestamp\x18\x02 \x01(\x03\x12\x0e\n\x06sample\x18\x03 \x01(\x0c\"9\n\x11SpectrumComponent\x12\x11\n\tfrequency\x18\x01 \x01(\x05\x12\x11\n\tamplitude\x18\x02 \x01(\x05\"a\n\x08Spectrum\x12\n\n\x02id\x18\x01 \x01(\x11\x12\x11\n\ttimestamp\x18\x02 \x01(\x03\x12\x36\n\ncomponents\x18\x03 \x03(\x0b\x32\".audio_processor.SpectrumComponent\"x\n\x0c\x41udioSummary\x12\n\n\x02id\x18\x01 \x01(\x11\x12\x11\n\ttimestamp\x18\x02 \x01(\x03\x12\x11\n\trmsVolume\x18\x03 \x01(\x05\x12\x36\n\ncomponents\x18\x04 \x03(\x0b\x32\".audio_processor.SpectrumComponent2\xb6\x01\n\rAudioAnalyzer\x12P\n\x0f\x43omputeSpectrum\x12\x1c.audio_processor.AudioSample\x1a\x19.audio_processor.Spectrum\"\x00(\x01\x30\x01\x12S\n\x0e\x43omputeSummary\x12\x1c.audio_processor.AudioSample\x1a\x1d.audio_processor.AudioSummary\"\x00(\x01\x30\x01\x62\x06proto3'
)




_AUDIOSAMPLE = _descriptor.Descriptor(
  name='AudioSample',
  full_name='audio_processor.AudioSample',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='audio_processor.AudioSample.id', index=0,
      number=1, type=17, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='timestamp', full_name='audio_processor.AudioSample.timestamp', index=1,
      number=2, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='sample', full_name='audio_processor.AudioSample.sample', index=2,
      number=3, type=12, cpp_type=9, label=1,
      has_default_value=False, default_value=b"",
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=42,
  serialized_end=102,
)


_SPECTRUMCOMPONENT = _descriptor.Descriptor(
  name='SpectrumComponent',
  full_name='audio_processor.SpectrumComponent',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='frequency', full_name='audio_processor.SpectrumComponent.frequency', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='amplitude', full_name='audio_processor.SpectrumComponent.amplitude', index=1,
      number=2, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=104,
  serialized_end=161,
)


_SPECTRUM = _descriptor.Descriptor(
  name='Spectrum',
  full_name='audio_processor.Spectrum',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='audio_processor.Spectrum.id', index=0,
      number=1, type=17, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='timestamp', full_name='audio_processor.Spectrum.timestamp', index=1,
      number=2, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='components', full_name='audio_processor.Spectrum.components', index=2,
      number=3, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=163,
  serialized_end=260,
)


_AUDIOSUMMARY = _descriptor.Descriptor(
  name='AudioSummary',
  full_name='audio_processor.AudioSummary',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  create_key=_descriptor._internal_create_key,
  fields=[
    _descriptor.FieldDescriptor(
      name='id', full_name='audio_processor.AudioSummary.id', index=0,
      number=1, type=17, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='timestamp', full_name='audio_processor.AudioSummary.timestamp', index=1,
      number=2, type=3, cpp_type=2, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='rmsVolume', full_name='audio_processor.AudioSummary.rmsVolume', index=2,
      number=3, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
    _descriptor.FieldDescriptor(
      name='components', full_name='audio_processor.AudioSummary.components', index=3,
      number=4, type=11, cpp_type=10, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      serialized_options=None, file=DESCRIPTOR,  create_key=_descriptor._internal_create_key),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  serialized_options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=262,
  serialized_end=382,
)

_SPECTRUM.fields_by_name['components'].message_type = _SPECTRUMCOMPONENT
_AUDIOSUMMARY.fields_by_name['components'].message_type = _SPECTRUMCOMPONENT
DESCRIPTOR.message_types_by_name['AudioSample'] = _AUDIOSAMPLE
DESCRIPTOR.message_types_by_name['SpectrumComponent'] = _SPECTRUMCOMPONENT
DESCRIPTOR.message_types_by_name['Spectrum'] = _SPECTRUM
DESCRIPTOR.message_types_by_name['AudioSummary'] = _AUDIOSUMMARY
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

AudioSample = _reflection.GeneratedProtocolMessageType('AudioSample', (_message.Message,), {
  'DESCRIPTOR' : _AUDIOSAMPLE,
  '__module__' : 'audio_processor_pb2'
  # @@protoc_insertion_point(class_scope:audio_processor.AudioSample)
  })
_sym_db.RegisterMessage(AudioSample)

SpectrumComponent = _reflection.GeneratedProtocolMessageType('SpectrumComponent', (_message.Message,), {
  'DESCRIPTOR' : _SPECTRUMCOMPONENT,
  '__module__' : 'audio_processor_pb2'
  # @@protoc_insertion_point(class_scope:audio_processor.SpectrumComponent)
  })
_sym_db.RegisterMessage(SpectrumComponent)

Spectrum = _reflection.GeneratedProtocolMessageType('Spectrum', (_message.Message,), {
  'DESCRIPTOR' : _SPECTRUM,
  '__module__' : 'audio_processor_pb2'
  # @@protoc_insertion_point(class_scope:audio_processor.Spectrum)
  })
_sym_db.RegisterMessage(Spectrum)

AudioSummary = _reflection.GeneratedProtocolMessageType('AudioSummary', (_message.Message,), {
  'DESCRIPTOR' : _AUDIOSUMMARY,
  '__module__' : 'audio_processor_pb2'
  # @@protoc_insertion_point(class_scope:audio_processor.AudioSummary)
  })
_sym_db.RegisterMessage(AudioSummary)



_AUDIOANALYZER = _descriptor.ServiceDescriptor(
  name='AudioAnalyzer',
  full_name='audio_processor.AudioAnalyzer',
  file=DESCRIPTOR,
  index=0,
  serialized_options=None,
  create_key=_descriptor._internal_create_key,
  serialized_start=385,
  serialized_end=567,
  methods=[
  _descriptor.MethodDescriptor(
    name='ComputeSpectrum',
    full_name='audio_processor.AudioAnalyzer.ComputeSpectrum',
    index=0,
    containing_service=None,
    input_type=_AUDIOSAMPLE,
    output_type=_SPECTRUM,
    serialized_options=None,
    create_key=_descriptor._internal_create_key,
  ),
  _descriptor.MethodDescriptor(
    name='ComputeSummary',
    full_name='audio_processor.AudioAnalyzer.ComputeSummary',
    index=1,
    containing_service=None,
    input_type=_AUDIOSAMPLE,
    output_type=_AUDIOSUMMARY,
    serialized_options=None,
    create_key=_descriptor._internal_create_key,
  ),
])
_sym_db.RegisterServiceDescriptor(_AUDIOANALYZER)

DESCRIPTOR.services_by_name['AudioAnalyzer'] = _AUDIOANALYZER

# @@protoc_insertion_point(module_scope)
